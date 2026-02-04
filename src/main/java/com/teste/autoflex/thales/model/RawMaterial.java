package com.teste.autoflex.thales.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Table(name = "raw_materials")
@Entity
@Data
public class RawMaterial {

    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    private UUID id;

    @Column
    private String name;

    @Column
    private Double stockQuantity;
}
