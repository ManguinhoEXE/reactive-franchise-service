package com.pruebadev.reactiveapi.application.service.impl;

import com.pruebadev.reactiveapi.application.dto.CreateSucursalDTO;
import com.pruebadev.reactiveapi.application.dto.SucursalResponseDTO;
import com.pruebadev.reactiveapi.application.dto.UpdateNameDTO;
import com.pruebadev.reactiveapi.application.service.SucursalService;
import com.pruebadev.reactiveapi.domain.model.Sucursal;
import com.pruebadev.reactiveapi.infrastructure.exception.EntityNotFoundException;
import com.pruebadev.reactiveapi.infrastructure.mapper.SucursalMapper;
import com.pruebadev.reactiveapi.infrastructure.persistence.repository.SucursalRepository;
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
public class SucursalServiceImpl implements SucursalService {

    private final SucursalRepository sucursalRepository;
    private final SucursalMapper sucursalMapper;

    @Override
    public Mono<SucursalResponseDTO> create(CreateSucursalDTO dto) {
        return Mono.fromCallable(() -> {
            Sucursal sucursal = sucursalMapper.dtoToDomain(dto);
            sucursal.setCreatedAt(Instant.now());
            sucursal.setUpdatedAt(Instant.now());
            sucursal.setActiva(true);
            return sucursal;
        })
        .flatMap(sucursal -> sucursalRepository.save(sucursalMapper.domainToEntity(sucursal))
                .map(sucursalMapper::entityToDomain)
                .map(sucursalMapper::toResponseDTO)
                .doOnSuccess(response -> log.info("Sucursal creada: {}", response.getId()))
                .doOnError(error -> log.error("Error al crear sucursal", error)));
    }

    @Override
    public Mono<SucursalResponseDTO> getById(UUID id) {
        return sucursalRepository.findById(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Sucursal no encontrada con ID: " + id)))
                .map(sucursalMapper::entityToDomain)
                .map(sucursalMapper::toResponseDTO)
                .doOnSuccess(response -> log.info("Sucursal obtenida: {}", id))
                .doOnError(error -> log.error("Error al obtener sucursal", error));
    }

    @Override
    public Flux<SucursalResponseDTO> getAll() {
        return sucursalRepository.findAll()
                .map(sucursalMapper::entityToDomain)
                .map(sucursalMapper::toResponseDTO)
                .doOnComplete(() -> log.info("Se obtuvieron todas las sucursales"))
                .doOnError(error -> log.error("Error al obtener sucursales", error));
    }

    @Override
    public Flux<SucursalResponseDTO> getByFranquiciaId(UUID franquiciaId) {
        return sucursalRepository.findAll()
                .filter(entity -> entity.getFranquiciaId().equals(franquiciaId))
                .map(sucursalMapper::entityToDomain)
                .map(sucursalMapper::toResponseDTO)
                .doOnComplete(() -> log.info("Se obtuvieron sucursales para franquicia: {}", franquiciaId))
                .doOnError(error -> log.error("Error al obtener sucursales por franquicia", error));
    }

    @Override
    public Mono<SucursalResponseDTO> updateNombre(UUID id, UpdateNameDTO dto) {
        return sucursalRepository.findById(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Sucursal no encontrada con ID: " + id)))
                .flatMap(entity -> {
                    entity.setNombre(dto.getNombre());
                    entity.setUpdatedAt(Instant.now());
                    return sucursalRepository.save(entity);
                })
                .map(sucursalMapper::entityToDomain)
                .map(sucursalMapper::toResponseDTO)
                .doOnSuccess(response -> log.info("Sucursal actualizada: {}", id))
                .doOnError(error -> log.error("Error al actualizar sucursal", error));
    }

    @Override
    public Mono<Void> delete(UUID id) {
        return sucursalRepository.deleteById(id)
                .doOnSuccess(unused -> log.info("Sucursal eliminada: {}", id))
                .doOnError(error -> log.error("Error al eliminar sucursal", error));
    }
}
