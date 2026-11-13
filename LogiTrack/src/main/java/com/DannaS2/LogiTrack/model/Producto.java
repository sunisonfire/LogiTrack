package com.DannaS2.LogiTrack.model;

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
@AllArgsConstructor
@NoArgsConstructor
public class Producto {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false, length = 50)
        private String nombre;

        @Column(name="descripcion", length = 100)
        private String descripcion;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private Categoria categoria;

        @Column(name = "precio", nullable = false, precision = 12, scale = 2)
        private BigDecimal precio;

        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "marca_id", nullable = false)
        private Marca marca;

}
