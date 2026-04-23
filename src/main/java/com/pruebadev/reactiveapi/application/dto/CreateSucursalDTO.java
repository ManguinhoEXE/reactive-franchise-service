package com.pruebadev.reactiveapi.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateSucursalDTO {

    @NotNull(message = "El ID de la franquicia no puede ser nulo")
    private UUID franquiciaId;

    @NotBlank(message = "El nombre de la sucursal no puede estar vacío")
    @Size(min = 3, max = 150, message = "El nombre debe tener entre 3 y 150 caracteres")
    private String nombre;

    @NotBlank(message = "La dirección no puede estar vacía")
    @Size(min = 5, max = 255, message = "La dirección debe tener entre 5 y 255 caracteres")
    private String direccion;

    @NotBlank(message = "La ciudad no puede estar vacía")
    @Size(min = 2, max = 100, message = "La ciudad debe tener entre 2 y 100 caracteres")
    private String ciudad;

    @Size(max = 30, message = "El teléfono no puede exceder 30 caracteres")
    private String telefono;
}
