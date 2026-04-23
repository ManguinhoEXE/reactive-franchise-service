package com.pruebadev.reactiveapi.application.service.impl;

import com.pruebadev.reactiveapi.application.dto.CreateFranquiciaDTO;
import com.pruebadev.reactiveapi.application.dto.FranquiciaResponseDTO;
import com.pruebadev.reactiveapi.application.dto.UpdateNameDTO;
import com.pruebadev.reactiveapi.application.service.FranquiciaService;
import com.pruebadev.reactiveapi.domain.model.Franquicia;
import com.pruebadev.reactiveapi.infrastructure.exception.EntityNotFoundException;
import com.pruebadev.reactiveapi.infrastructure.mapper.FranquiciaMapper;
import com.pruebadev.reactiveapi.infrastructure.persistence.repository.FranquiciaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FranquiciaServiceImpl implements FranquiciaService {

    private final FranquiciaRepository franquiciaRepository;
    private final FranquiciaMapper franquiciaMapper;

    @Override
    public Mono<FranquiciaResponseDTO> create(CreateFranquiciaDTO dto) {
        return Mono.fromCallable(() -> {
            Franquicia franquicia = franquiciaMapper.dtoToDomain(dto);
            franquicia.setCreatedAt(Instant.now());
            franquicia.setUpdatedAt(Instant.now());
            franquicia.setActiva(true);
            return franquicia;
        })
        .flatMap(franquicia -> franquiciaRepository.save(franquiciaMapper.domainToEntity(franquicia))
                .map(franquiciaMapper::entityToDomain)
                .map(franquiciaMapper::toResponseDTO)
                .doOnSuccess(response -> log.info("Franquicia creada: {}", response.getId()))
                .doOnError(error -> log.error("Error al crear franquicia", error)));
    }

    @Override
    public Mono<FranquiciaResponseDTO> getById(UUID id) {
        return franquiciaRepository.findById(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Franquicia no encontrada con ID: " + id)))
                .map(franquiciaMapper::entityToDomain)
                .map(franquiciaMapper::toResponseDTO)
                .doOnSuccess(response -> log.info("Franquicia obtenida: {}", id))
                .doOnError(error -> log.error("Error al obtener franquicia", error));
    }

    @Override
    public Flux<FranquiciaResponseDTO> getAll() {
        return franquiciaRepository.findAll()
                .map(franquiciaMapper::entityToDomain)
                .map(franquiciaMapper::toResponseDTO)
                .doOnComplete(() -> log.info("Se obtuvieron todas las franquicias"))
                .doOnError(error -> log.error("Error al obtener franquicias", error));
    }

    @Override
    public Mono<FranquiciaResponseDTO> updateNombre(UUID id, UpdateNameDTO dto) {
        return franquiciaRepository.findById(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Franquicia no encontrada con ID: " + id)))
                .flatMap(entity -> {
                    entity.setNombre(dto.getNombre());
                    entity.setUpdatedAt(Instant.now());
                    return franquiciaRepository.save(entity);
                })
                .map(franquiciaMapper::entityToDomain)
                .map(franquiciaMapper::toResponseDTO)
                .doOnSuccess(response -> log.info("Franquicia actualizada: {}", id))
                .doOnError(error -> log.error("Error al actualizar franquicia", error));
    }

    @Override
    public Mono<Void> delete(UUID id) {
        return franquiciaRepository.deleteById(id)
                .doOnSuccess(unused -> log.info("Franquicia eliminada: {}", id))
                .doOnError(error -> log.error("Error al eliminar franquicia", error));
    }
}
