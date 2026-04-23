package com.pruebadev.reactiveapi.application.service.impl;

import com.pruebadev.reactiveapi.application.dto.CreateSucursalDTO;
import com.pruebadev.reactiveapi.application.dto.SucursalResponseDTO;
import com.pruebadev.reactiveapi.infrastructure.exception.EntityNotFoundException;
import com.pruebadev.reactiveapi.infrastructure.mapper.SucursalMapper;
import com.pruebadev.reactiveapi.infrastructure.persistence.entity.SucursalEntity;
import com.pruebadev.reactiveapi.infrastructure.persistence.repository.SucursalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SucursalServiceImplTest {

    @Mock
    private SucursalRepository sucursalRepository;

    @Mock
    private SucursalMapper sucursalMapper;

    private SucursalServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new SucursalServiceImpl(sucursalRepository, sucursalMapper);
    }

    @Test
    void getById_whenMissing_shouldEmitNotFound() {
        UUID id = UUID.randomUUID();
        when(sucursalRepository.findById(id)).thenReturn(Mono.empty());

        StepVerifier.create(service.getById(id))
                .expectError(EntityNotFoundException.class)
                .verify();
    }

    @Test
    void getByFranquiciaId_shouldFilterByFranquicia() {
        UUID franquiciaId = UUID.randomUUID();
        UUID otherId = UUID.randomUUID();

        SucursalEntity s1 = SucursalEntity.builder().id(UUID.randomUUID()).franquiciaId(franquiciaId).nombre("Centro").build();
        SucursalEntity s2 = SucursalEntity.builder().id(UUID.randomUUID()).franquiciaId(otherId).nombre("Norte").build();

        SucursalResponseDTO r1 = SucursalResponseDTO.builder().id(s1.getId()).franquiciaId(franquiciaId).nombre("Centro").build();

        when(sucursalRepository.findAll()).thenReturn(Flux.just(s1, s2));
        when(sucursalMapper.entityToDomain(s1)).thenReturn(com.pruebadev.reactiveapi.domain.model.Sucursal.builder().id(s1.getId()).franquiciaId(franquiciaId).nombre("Centro").build());
        when(sucursalMapper.toResponseDTO(any())).thenReturn(r1);

        StepVerifier.create(service.getByFranquiciaId(franquiciaId))
                .expectNextMatches(item -> franquiciaId.equals(item.getFranquiciaId()))
                .verifyComplete();
    }

    @Test
    void delete_shouldComplete() {
        UUID id = UUID.randomUUID();
        when(sucursalRepository.deleteById(id)).thenReturn(Mono.empty());

        StepVerifier.create(service.delete(id))
                .verifyComplete();
    }

    @Test
    void create_shouldEmitResponse() {
        CreateSucursalDTO dto = CreateSucursalDTO.builder()
                .franquiciaId(UUID.randomUUID())
                .nombre("Centro")
                .direccion("Calle 1")
                .ciudad("Bogota")
                .telefono("3000000000")
                .build();

        com.pruebadev.reactiveapi.domain.model.Sucursal mapped = com.pruebadev.reactiveapi.domain.model.Sucursal.builder().nombre("Centro").build();
        SucursalEntity saved = SucursalEntity.builder().id(UUID.randomUUID()).nombre("Centro").build();
        SucursalResponseDTO response = SucursalResponseDTO.builder().id(saved.getId()).nombre("Centro").build();

        when(sucursalMapper.dtoToDomain(dto)).thenReturn(mapped);
        when(sucursalMapper.domainToEntity(any())).thenReturn(saved);
        when(sucursalRepository.save(any())).thenReturn(Mono.just(saved));
        when(sucursalMapper.entityToDomain(saved)).thenReturn(com.pruebadev.reactiveapi.domain.model.Sucursal.builder().id(saved.getId()).nombre("Centro").build());
        when(sucursalMapper.toResponseDTO(any())).thenReturn(response);

        StepVerifier.create(service.create(dto))
                .expectNextMatches(item -> "Centro".equals(item.getNombre()))
                .verifyComplete();
    }
}
