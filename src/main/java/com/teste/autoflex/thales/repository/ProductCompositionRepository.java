package com.teste.autoflex.thales.repository;

import com.teste.autoflex.thales.model.ProductComposition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductCompositionRepository extends JpaRepository<ProductComposition, UUID> {
}
