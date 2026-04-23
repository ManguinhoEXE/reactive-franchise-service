package com.pruebadev.reactiveapi.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class SucursalResponseDTO {

    private UUID id;
    private UUID franquiciaId;
    private String nombre;
    private String direccion;
    private String ciudad;
    private String telefono;
    private boolean activa;

    @JsonProperty("createdAt")
    private Instant createdAt;

    @JsonProperty("updatedAt")
    private Instant updatedAt;
}
