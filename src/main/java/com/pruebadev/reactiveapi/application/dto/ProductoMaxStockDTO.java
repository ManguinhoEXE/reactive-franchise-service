package com.pruebadev.reactiveapi.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoMaxStockDTO {

    private String sucursalNombre;
    private String productoNombre;
    private String productoDescripcion;
    private BigDecimal productoPrecio;
    private Integer stock;
}
