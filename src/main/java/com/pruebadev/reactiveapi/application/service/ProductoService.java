package com.pruebadev.reactiveapi.application.service;

import com.pruebadev.reactiveapi.application.dto.CreateProductoDTO;
import com.pruebadev.reactiveapi.application.dto.ProductoMaxStockDTO;
import com.pruebadev.reactiveapi.application.dto.ProductoResponseDTO;
import com.pruebadev.reactiveapi.application.dto.UpdateNameDTO;
import com.pruebadev.reactiveapi.application.dto.UpdateStockDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ProductoService {

    Mono<ProductoResponseDTO> create(CreateProductoDTO dto);

    Mono<ProductoResponseDTO> getById(UUID id);

    Flux<ProductoResponseDTO> getAll();

    Flux<ProductoResponseDTO> getBySucursalId(UUID sucursalId);

    Mono<ProductoResponseDTO> updateStock(UUID id, UpdateStockDTO dto);

    Mono<ProductoResponseDTO> updateNombre(UUID id, UpdateNameDTO dto);

    Mono<Void> delete(UUID id);

    Flux<ProductoMaxStockDTO> getMaxStockByFranquicia(UUID franquiciaId);
}
