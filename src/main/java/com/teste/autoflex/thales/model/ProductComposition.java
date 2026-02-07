package com.teste.autoflex.thales.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Table(name = "product_compositions")
@Entity
@Data
public class ProductComposition {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private Product product;

    @ManyToOne
    @JoinColumn(name = "raw_material_id")
    private RawMaterial rawMaterial;

    @Column
    private Double requiredQuantity;
}
