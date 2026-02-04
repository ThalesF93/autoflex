package com.teste.autoflex.thales.repository;

import com.teste.autoflex.thales.model.RawMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RawMaterialRepository extends JpaRepository<RawMaterial, UUID> {
}
