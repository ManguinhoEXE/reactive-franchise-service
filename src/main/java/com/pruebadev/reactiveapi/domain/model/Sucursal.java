package com.pruebadev.reactiveapi.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sucursal {

    private UUID id;
    private UUID franquiciaId;
    private String nombre;
    private String direccion;
    private String ciudad;
    private String telefono;
    private boolean activa;
    private Instant createdAt;
    private Instant updatedAt;
}