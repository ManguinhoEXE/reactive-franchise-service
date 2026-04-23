package com.pruebadev.reactiveapi.application.service;

import com.pruebadev.reactiveapi.application.dto.CreateSucursalDTO;
import com.pruebadev.reactiveapi.application.dto.SucursalResponseDTO;
import com.pruebadev.reactiveapi.application.dto.UpdateNameDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface SucursalService {

    Mono<SucursalResponseDTO> create(CreateSucursalDTO dto);

    Mono<SucursalResponseDTO> getById(UUID id);

    Flux<SucursalResponseDTO> getAll();

    Flux<SucursalResponseDTO> getByFranquiciaId(UUID franquiciaId);

    Mono<SucursalResponseDTO> updateNombre(UUID id, UpdateNameDTO dto);

    Mono<Void> delete(UUID id);
}
