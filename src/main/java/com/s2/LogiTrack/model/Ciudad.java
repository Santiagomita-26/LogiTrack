package com.s2.LogiTrack.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table ( name = "ciudad")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class Ciudad {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    @Column(
            nullable = false,
            length = 100
    )
    private String nombre;

    @Column(
            nullable = false,
            length = 100
    )
    private String departamento;

}
