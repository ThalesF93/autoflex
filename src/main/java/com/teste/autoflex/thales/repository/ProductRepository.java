package com.teste.autoflex.thales.repository;

import com.teste.autoflex.thales.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    boolean existsProductByNameIgnoreCase(String name);
}
