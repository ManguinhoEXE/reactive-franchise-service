package com.pruebadev.reactiveapi.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    private UUID id;
    private UUID sucursalId;
    private String nombre;
    private String sku;
    private String descripcion;
    private BigDecimal precio;
    private Integer stock;
    private boolean activo;
    private Instant createdAt;
    private Instant updatedAt;
}