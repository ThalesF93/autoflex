package com.teste.autoflex.thales.service;

import com.teste.autoflex.thales.dto.ProductDTO;
import com.teste.autoflex.thales.exceptions.MaterialNotFoundException;
import com.teste.autoflex.thales.model.Product;
import com.teste.autoflex.thales.model.ProductComposition;
import com.teste.autoflex.thales.model.RawMaterial;
import com.teste.autoflex.thales.repository.ProductRepository;
import com.teste.autoflex.thales.repository.RawMaterialRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
public class ProductService {

    private ProductRepository productRepository;

    private RawMaterialRepository rawMaterialRepository;

    public Product save(ProductDTO dto){
        Product product = new Product();
        product.setName(dto.name());
        product.setPrice(dto.price());

        List<ProductComposition> compositions = dto.ingredients()
                .stream()
                .map(ing -> {
                    RawMaterial material = rawMaterialRepository.findById(ing.rawMaterialId())
                            .orElseThrow(()-> new MaterialNotFoundException("Material not Found"));

                    ProductComposition comp = new ProductComposition();
                    comp.setProduct(product);
                    comp.setRawMaterial(material);
                    comp.setRequiredQuantity(ing.quantity());
                    return comp;
                }).toList();

        product.setCompositions(compositions);
        return productRepository.save(product);
    }
}
