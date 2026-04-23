package com.pruebadev.reactiveapi.application.service;

import com.pruebadev.reactiveapi.application.dto.CreateFranquiciaDTO;
import com.pruebadev.reactiveapi.application.dto.FranquiciaResponseDTO;
import com.pruebadev.reactiveapi.application.dto.UpdateNameDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface FranquiciaService {

    Mono<FranquiciaResponseDTO> create(CreateFranquiciaDTO dto);

    Mono<FranquiciaResponseDTO> getById(UUID id);

    Flux<FranquiciaResponseDTO> getAll();

    Mono<FranquiciaResponseDTO> updateNombre(UUID id, UpdateNameDTO dto);

    Mono<Void> delete(UUID id);
}
