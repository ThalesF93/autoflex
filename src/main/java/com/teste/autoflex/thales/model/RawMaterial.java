package com.teste.autoflex.thales.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Table(name = "raw_materials")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RawMaterial {

    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    private UUID id;

    @Column
    private String name;

    @Column
    private Double stockQuantity;

}
