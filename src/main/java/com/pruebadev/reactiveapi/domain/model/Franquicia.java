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
public class Franquicia {

    private UUID id;
    private String nombre;
    private String codigo;
    private String descripcion;
    private boolean activa;
    private Instant createdAt;
    private Instant updatedAt;
}