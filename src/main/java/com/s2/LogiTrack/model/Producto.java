package com.s2.LogiTrack.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "producto")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (
            nullable = false,
            length = 100
    )
    private String nombre;

    @Column(
            nullable = false,
            length = 100
    )
    private String categoria;

    @Column(
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal precio;

    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            nullable = false,
            name = "marca_fk"
    )
    private Marca marca;

}
