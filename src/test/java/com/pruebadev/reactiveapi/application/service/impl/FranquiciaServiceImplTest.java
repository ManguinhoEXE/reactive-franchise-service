package com.pruebadev.reactiveapi.application.service.impl;

import com.pruebadev.reactiveapi.application.dto.CreateFranquiciaDTO;
import com.pruebadev.reactiveapi.application.dto.FranquiciaResponseDTO;
import com.pruebadev.reactiveapi.application.dto.UpdateNameDTO;
import com.pruebadev.reactiveapi.domain.model.Franquicia;
import com.pruebadev.reactiveapi.infrastructure.exception.EntityNotFoundException;
import com.pruebadev.reactiveapi.infrastructure.mapper.FranquiciaMapper;
import com.pruebadev.reactiveapi.infrastructure.persistence.entity.FranquiciaEntity;
import com.pruebadev.reactiveapi.infrastructure.persistence.repository.FranquiciaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Instant;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FranquiciaServiceImplTest {

    @Mock
    private FranquiciaRepository franquiciaRepository;

    @Mock
    private FranquiciaMapper franquiciaMapper;

    private FranquiciaServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new FranquiciaServiceImpl(franquiciaRepository, franquiciaMapper);
    }

    @Test
    void create_shouldEmitFranquiciaResponse() {
        CreateFranquiciaDTO dto = CreateFranquiciaDTO.builder()
                .nombre("Franquicia Uno")
                .codigo("FRQ-01")
                .descripcion("Descripcion")
                .build();

        Franquicia domain = Franquicia.builder().nombre("Franquicia Uno").codigo("FRQ-01").build();
        FranquiciaEntity savedEntity = FranquiciaEntity.builder()
                .id(UUID.randomUUID())
                .nombre("Franquicia Uno")
                .codigo("FRQ-01")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .activa(true)
                .build();
        Franquicia mappedDomain = Franquicia.builder().id(savedEntity.getId()).nombre("Franquicia Uno").codigo("FRQ-01").build();
        FranquiciaResponseDTO response = FranquiciaResponseDTO.builder()
                .id(savedEntity.getId())
                .nombre("Franquicia Uno")
                .codigo("FRQ-01")
                .activa(true)
                .build();

        when(franquiciaMapper.dtoToDomain(dto)).thenReturn(domain);
        when(franquiciaMapper.domainToEntity(any(Franquicia.class))).thenReturn(savedEntity);
        when(franquiciaRepository.save(any(FranquiciaEntity.class))).thenReturn(Mono.just(savedEntity));
        when(franquiciaMapper.entityToDomain(savedEntity)).thenReturn(mappedDomain);
        when(franquiciaMapper.toResponseDTO(mappedDomain)).thenReturn(response);

        StepVerifier.create(service.create(dto))
                .assertNext(result -> {
                    org.junit.jupiter.api.Assertions.assertEquals("Franquicia Uno", result.getNombre());
                    org.junit.jupiter.api.Assertions.assertEquals("FRQ-01", result.getCodigo());
                })
                .verifyComplete();
    }

    @Test
    void getById_whenMissing_shouldEmitEntityNotFoundError() {
        UUID id = UUID.randomUUID();
        when(franquiciaRepository.findById(id)).thenReturn(Mono.empty());

        StepVerifier.create(service.getById(id))
                .expectErrorSatisfies(error -> {
                    org.junit.jupiter.api.Assertions.assertInstanceOf(EntityNotFoundException.class, error);
                    org.junit.jupiter.api.Assertions.assertTrue(error.getMessage().contains(id.toString()));
                })
                .verify();
    }

    @Test
    void getAll_shouldEmitAllItems() {
        FranquiciaEntity e1 = FranquiciaEntity.builder().id(UUID.randomUUID()).nombre("F1").codigo("C1").activa(true).build();
        FranquiciaEntity e2 = FranquiciaEntity.builder().id(UUID.randomUUID()).nombre("F2").codigo("C2").activa(true).build();

        Franquicia d1 = Franquicia.builder().id(e1.getId()).nombre("F1").codigo("C1").activa(true).build();
        Franquicia d2 = Franquicia.builder().id(e2.getId()).nombre("F2").codigo("C2").activa(true).build();

        FranquiciaResponseDTO r1 = FranquiciaResponseDTO.builder().id(e1.getId()).nombre("F1").codigo("C1").activa(true).build();
        FranquiciaResponseDTO r2 = FranquiciaResponseDTO.builder().id(e2.getId()).nombre("F2").codigo("C2").activa(true).build();

        when(franquiciaRepository.findAll()).thenReturn(Flux.just(e1, e2));
        when(franquiciaMapper.entityToDomain(e1)).thenReturn(d1);
        when(franquiciaMapper.entityToDomain(e2)).thenReturn(d2);
        when(franquiciaMapper.toResponseDTO(d1)).thenReturn(r1);
        when(franquiciaMapper.toResponseDTO(d2)).thenReturn(r2);

        StepVerifier.create(service.getAll())
                .expectNextMatches(item -> "F1".equals(item.getNombre()))
                .expectNextMatches(item -> "F2".equals(item.getNombre()))
                .verifyComplete();
    }

    @Test
    void updateNombre_shouldUpdateAndEmitResponse() {
        UUID id = UUID.randomUUID();
        UpdateNameDTO dto = UpdateNameDTO.builder().nombre("Nuevo Nombre").build();

        FranquiciaEntity existing = FranquiciaEntity.builder().id(id).nombre("Viejo").codigo("C1").activa(true).build();
        FranquiciaEntity updated = FranquiciaEntity.builder().id(id).nombre("Nuevo Nombre").codigo("C1").activa(true).build();
        Franquicia mapped = Franquicia.builder().id(id).nombre("Nuevo Nombre").codigo("C1").activa(true).build();
        FranquiciaResponseDTO response = FranquiciaResponseDTO.builder().id(id).nombre("Nuevo Nombre").codigo("C1").activa(true).build();

        when(franquiciaRepository.findById(id)).thenReturn(Mono.just(existing));
        when(franquiciaRepository.save(any(FranquiciaEntity.class))).thenReturn(Mono.just(updated));
        when(franquiciaMapper.entityToDomain(updated)).thenReturn(mapped);
        when(franquiciaMapper.toResponseDTO(mapped)).thenReturn(response);

        StepVerifier.create(service.updateNombre(id, dto))
                .expectNextMatches(item -> "Nuevo Nombre".equals(item.getNombre()))
                .verifyComplete();
    }
}
