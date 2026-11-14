package com.s2.LogiTrack.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table ( name = "movimiento")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class Movimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (
            nullable = false
    )
    private LocalDateTime fecha;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoMovimiento tipo;

    public enum TipoMovimiento {
        ENTRADA,
        SALIDA,
        TRANSFERENCIA
    }

    @Column (
            nullable = false
    )
    private Integer cantidad;


    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            nullable = false,
            name = "usuario_fk"
    )
    private Usuario usuario;

    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            nullable = false,
            name = "producto_fk"
    )
    private Producto producto;


    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "bodega_origen_fk"
    )
    private Bodega bodegaOrigen;

    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "bodega_destino_fk"
    )
    private Bodega bodegaDestino;

}
