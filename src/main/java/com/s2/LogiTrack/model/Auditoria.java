package com.s2.LogiTrack.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import tools.jackson.databind.JsonNode;

import java.time.LocalDateTime;

@Entity
@Table( name = "auditoria")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class Auditoria {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoOperacion operacion;

    public enum TipoOperacion {
        INSERT,
        UPDATE,
        DELETE
    }

    @Column(
            nullable = false
    )
    private LocalDateTime fecha;


    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            nullable = false,
            name = "usuario_fk"
    )
    private Usuario usuario;

    @Column (
            nullable = false ,
            length = 100
    )
    private String entidad;


    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "valores_anteriores", columnDefinition = "json")
    private JsonNode valoresAnteriores;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "valores_nuevos", columnDefinition = "json")
    private JsonNode valoresNuevos;

}
