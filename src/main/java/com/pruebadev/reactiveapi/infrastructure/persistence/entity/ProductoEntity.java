package com.pruebadev.reactiveapi.infrastructure.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("producto")
public class ProductoEntity {

    @Id
    @Column("id")
    private UUID id;
    @Column("sucursal_id")
    private UUID sucursalId;
    @Column("nombre")
    private String nombre;
    @Column("sku")
    private String sku;
    @Column("descripcion")
    private String descripcion;
    @Column("precio")
    private BigDecimal precio;
    @Column("stock")
    private Integer stock;
    @Column("activo")
    private boolean activo;
    @Column("created_at")
    private Instant createdAt;
    @Column("updated_at")
    private Instant updatedAt;
}