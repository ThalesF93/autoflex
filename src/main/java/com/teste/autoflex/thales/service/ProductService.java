package com.teste.autoflex.thales.service;

import com.teste.autoflex.thales.dto.ProductDTO;
import com.teste.autoflex.thales.dto.response.IngredientResponseDTO;
import com.teste.autoflex.thales.dto.response.ProductResponseDTO;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

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
}
