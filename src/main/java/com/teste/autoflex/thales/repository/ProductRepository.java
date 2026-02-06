package com.teste.autoflex.thales.repository;

import com.teste.autoflex.thales.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    boolean existsProductByNameIgnoreCase(String name);

    List<Product> findAllByOrderByNameAsc();
}
