package com.pruebadev.reactiveapi.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateFranquiciaDTO {

    @NotBlank(message = "El nombre de la franquicia no puede estar vacío")
    @Size(min = 3, max = 150, message = "El nombre debe tener entre 3 y 150 caracteres")
    private String nombre;

    @NotBlank(message = "El código de la franquicia no puede estar vacío")
    @Size(min = 2, max = 50, message = "El código debe tener entre 2 y 50 caracteres")
    private String codigo;

    private String descripcion;
}
