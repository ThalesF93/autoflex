package com.teste.autoflex.thales.service;

import com.teste.autoflex.thales.dto.suggestionDTO.ProductionReportDTO;
import com.teste.autoflex.thales.dto.suggestionDTO.ProductionSuggestionDTO;
import com.teste.autoflex.thales.dto.entitiesDTO.ProductDTO;
import com.teste.autoflex.thales.dto.response.IngredientResponseDTO;
import com.teste.autoflex.thales.dto.response.ProductResponseDTO;
import com.teste.autoflex.thales.dto.update.IngredientUpdateDTO;
import com.teste.autoflex.thales.dto.update.ProductUpdateDTO;
import com.teste.autoflex.thales.exceptions.DuplicatedRegisterException;
import com.teste.autoflex.thales.exceptions.MaterialNotFoundException;
import com.teste.autoflex.thales.exceptions.ProductNotFoundException;
import com.teste.autoflex.thales.model.Product;
import com.teste.autoflex.thales.model.ProductComposition;
import com.teste.autoflex.thales.model.RawMaterial;
import com.teste.autoflex.thales.repository.ProductRepository;
import com.teste.autoflex.thales.repository.RawMaterialRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
@Data
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private RawMaterialRepository rawMaterialRepository;

    @Transactional
    public ProductResponseDTO save(ProductDTO dto){

        if (productRepository.existsProductByNameIgnoreCase(dto.name()))
        {
            throw new DuplicatedRegisterException("Product with name: " + dto.name() + " already exists");
        }
        Product product = new Product();
        product.setName(dto.name());
        product.setPrice(dto.price());

        List<ProductComposition> compositions = dto.ingredients()
                .stream()
                .map(ing -> {
                    RawMaterial material = rawMaterialRepository.findById(ing.rawMaterialId())
                            .orElseThrow(()-> new MaterialNotFoundException("Material not Found"));

                    ProductComposition composition = new ProductComposition();
                    composition.setProduct(product);
                    composition.setRawMaterial(material);
                    composition.setRequiredQuantity(ing.quantity());
                    return composition;
                }).toList();

        product.setCompositions(compositions);
        Product savedProduct = productRepository.save(product);

        return new ProductResponseDTO(
                savedProduct.getId(),
                savedProduct.getName(),
                savedProduct.getPrice(),
                savedProduct.getCompositions().stream()
                        .map(c ->
                                new IngredientResponseDTO(c.getRawMaterial().getName(), c.getRequiredQuantity()))
                        .toList()
        );
    }

    @Transactional
    public void delete(UUID id){
      var product =  productRepository.findById(id)
              .orElseThrow(()-> new ProductNotFoundException("Product Not found"));
       productRepository.delete(product);
    }

    public List<Product> listAll(){
        return productRepository.findAllByOrderByNameAsc();
    }

    public List<Product> search(String name){
        Product product = new Product();
        product.setName(name);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase()
                .withIgnoreNullValues();

        Example<Product> example = Example.of(product, matcher);

        return productRepository.findAll(example);
    }

    @Transactional
    public ProductUpdateDTO update(UUID id, ProductUpdateDTO dto){

    Product product = productRepository.findById(id).orElseThrow(()-> new ProductNotFoundException("Product not Found"));

    if (dto.name() != null && !dto.name().isBlank()){
        product.setName(dto.name());
    }

    if (dto.price() != null){
        product.setPrice(dto.price());
    }

    if (dto.ingredients() != null){
        product.getCompositions().clear();

        dto.ingredients().forEach(ingredientDTO -> {
            RawMaterial rawMaterial =
                    rawMaterialRepository.findById(ingredientDTO.rawMaterialId())
                            .orElseThrow(()-> new MaterialNotFoundException("Material not Found"));

            ProductComposition composition = new ProductComposition();
            composition.setProduct(product);
            composition.setRawMaterial(rawMaterial);
            composition.setRequiredQuantity(ingredientDTO.quantity());

            product.getCompositions().add(composition);
        });
    }
        Product updatedProduct = productRepository.save(product);

        var ingredientsResponse = updatedProduct.getCompositions().stream()
                .map(ing -> new IngredientUpdateDTO(
                        ing.getRawMaterial().getId(),
                        ing.getRawMaterial().getName(),
                        ing.getRequiredQuantity()))
                .toList();

        return new ProductUpdateDTO(updatedProduct.getName(), updatedProduct.getPrice(), ingredientsResponse);
    }


    public ProductionReportDTO getProductionSuggestions() {
        List<Product> products = productRepository.findAll();
        List<RawMaterial> materials = rawMaterialRepository.findAll();

        products.sort((p1, p2) -> p2.getPrice().compareTo(p1.getPrice()));

        Map<UUID, Double> tempStock = new HashMap<>();
        for (RawMaterial m : materials) {
            tempStock.put(m.getId(), m.getStockQuantity());
        }

        List<ProductionSuggestionDTO> suggestions = new ArrayList<>();
        BigDecimal grandTotalValue = BigDecimal.ZERO;

        for (Product product : products) {
            double possibleQuantity = 999999;

            for (ProductComposition composition : product.getCompositions()) {
                double available = tempStock.get(composition.getRawMaterial().getId());
                double required = composition.getRequiredQuantity();

                double canMake = (available / required);

                if (canMake < possibleQuantity) {
                    possibleQuantity = canMake;
                }
            }

            if (possibleQuantity > 0) {
                for (ProductComposition composition : product.getCompositions()) {
                    UUID rawMaterialId = composition.getRawMaterial().getId();
                    double spent = possibleQuantity * composition.getRequiredQuantity();

                    double remaining = tempStock.get(rawMaterialId) - spent;
                    tempStock.put(rawMaterialId, remaining);
                }

                BigDecimal subtotal = product.getPrice().multiply(BigDecimal.valueOf(possibleQuantity));
                grandTotalValue = grandTotalValue.add(subtotal);

                suggestions.add(new ProductionSuggestionDTO(
                        product.getName(),
                        possibleQuantity,
                        subtotal
                ));
            }
        }

        return new ProductionReportDTO(suggestions, grandTotalValue);

    }


    public static List<ProductResponseDTO> convertListToDTO(List<Product> list) {
        return list.stream()
                .map(p -> new ProductResponseDTO(
                        p.getId(),
                        p.getName(),
                        p.getPrice(),
                        p.getCompositions().stream()
                                .map(m -> new IngredientResponseDTO(
                                        m.getRawMaterial().getName(),
                                        m.getRequiredQuantity()))
                                .toList()))
                .toList();
    }

}
