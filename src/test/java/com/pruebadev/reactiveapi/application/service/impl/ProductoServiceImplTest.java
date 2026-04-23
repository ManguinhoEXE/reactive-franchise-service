package com.pruebadev.reactiveapi.application.service.impl;

import com.pruebadev.reactiveapi.application.dto.ProductoMaxStockDTO;
import com.pruebadev.reactiveapi.application.dto.UpdateStockDTO;
import com.pruebadev.reactiveapi.infrastructure.exception.EntityNotFoundException;
import com.pruebadev.reactiveapi.infrastructure.mapper.ProductoMapper;
import com.pruebadev.reactiveapi.infrastructure.persistence.entity.ProductoEntity;
import com.pruebadev.reactiveapi.infrastructure.persistence.entity.SucursalEntity;
import com.pruebadev.reactiveapi.infrastructure.persistence.repository.ProductoRepository;
import com.pruebadev.reactiveapi.infrastructure.persistence.repository.SucursalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductoServiceImplTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private SucursalRepository sucursalRepository;

    @Mock
    private ProductoMapper productoMapper;

    private ProductoServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ProductoServiceImpl(productoRepository, sucursalRepository, productoMapper);
    }

    @Test
    void updateStock_whenMissing_shouldEmitNotFound() {
        UUID id = UUID.randomUUID();
        when(productoRepository.findById(id)).thenReturn(Mono.empty());

        StepVerifier.create(service.updateStock(id, UpdateStockDTO.builder().stock(10).build()))
                .expectError(EntityNotFoundException.class)
                .verify();
    }

    @Test
    void getMaxStockByFranquicia_shouldEmitOnePerSucursal() {
        UUID franquiciaId = UUID.randomUUID();
        UUID sucursalId = UUID.randomUUID();

        SucursalEntity sucursal = SucursalEntity.builder()
                .id(sucursalId)
                .franquiciaId(franquiciaId)
                .nombre("Sucursal Centro")
                .build();

        ProductoEntity p1 = ProductoEntity.builder()
                .id(UUID.randomUUID())
                .sucursalId(sucursalId)
                .nombre("Producto A")
                .precio(new BigDecimal("10.00"))
                .stock(5)
                .build();

        ProductoEntity p2 = ProductoEntity.builder()
                .id(UUID.randomUUID())
                .sucursalId(sucursalId)
                .nombre("Producto B")
                .precio(new BigDecimal("15.00"))
                .stock(9)
                .build();

        when(sucursalRepository.findAll()).thenReturn(Flux.just(sucursal));
        when(productoRepository.findAll()).thenReturn(Flux.just(p1, p2));

        StepVerifier.create(service.getMaxStockByFranquicia(franquiciaId))
                .assertNext(item -> {
                    org.junit.jupiter.api.Assertions.assertEquals("Sucursal Centro", item.getSucursalNombre());
                    org.junit.jupiter.api.Assertions.assertEquals("Producto B", item.getProductoNombre());
                    org.junit.jupiter.api.Assertions.assertEquals(9, item.getStock());
                })
                .verifyComplete();
    }

    @Test
    void getBySucursalId_shouldReturnFilteredFlux() {
        UUID sucursalId = UUID.randomUUID();

        ProductoEntity p1 = ProductoEntity.builder().id(UUID.randomUUID()).sucursalId(sucursalId).nombre("A").stock(1).build();
        ProductoEntity p2 = ProductoEntity.builder().id(UUID.randomUUID()).sucursalId(UUID.randomUUID()).nombre("B").stock(2).build();

        var domain = com.pruebadev.reactiveapi.domain.model.Producto.builder().id(p1.getId()).sucursalId(sucursalId).nombre("A").stock(1).build();
        var response = com.pruebadev.reactiveapi.application.dto.ProductoResponseDTO.builder().id(p1.getId()).sucursalId(sucursalId).nombre("A").stock(1).build();

        when(productoRepository.findAll()).thenReturn(Flux.just(p1, p2));
        when(productoMapper.entityToDomain(p1)).thenReturn(domain);
        when(productoMapper.toResponseDTO(domain)).thenReturn(response);

        StepVerifier.create(service.getBySucursalId(sucursalId))
                .expectNextMatches(item -> sucursalId.equals(item.getSucursalId()) && "A".equals(item.getNombre()))
                .verifyComplete();
    }
}
