package com.pruebadev.reactiveapi.application.service.impl;

import com.pruebadev.reactiveapi.application.dto.CreateProductoDTO;
import com.pruebadev.reactiveapi.application.dto.ProductoMaxStockDTO;
import com.pruebadev.reactiveapi.application.dto.ProductoResponseDTO;
import com.pruebadev.reactiveapi.application.dto.UpdateNameDTO;
import com.pruebadev.reactiveapi.application.dto.UpdateStockDTO;
import com.pruebadev.reactiveapi.application.service.ProductoService;
import com.pruebadev.reactiveapi.domain.model.Producto;
import com.pruebadev.reactiveapi.infrastructure.exception.EntityNotFoundException;
import com.pruebadev.reactiveapi.infrastructure.mapper.ProductoMapper;
import com.pruebadev.reactiveapi.infrastructure.persistence.repository.ProductoRepository;
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
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final SucursalRepository sucursalRepository;
    private final ProductoMapper productoMapper;

    @Override
    public Mono<ProductoResponseDTO> create(CreateProductoDTO dto) {
        return sucursalRepository.findById(dto.getSucursalId())
            .switchIfEmpty(Mono.error(new EntityNotFoundException(
                "Sucursal no encontrada con ID: " + dto.getSucursalId())))
            .then(Mono.fromCallable(() -> {
                Producto producto = productoMapper.dtoToDomain(dto);
                producto.setCreatedAt(Instant.now());
                producto.setUpdatedAt(Instant.now());
                producto.setActivo(true);
                return producto;
            }))
            .flatMap(producto -> productoRepository.save(productoMapper.domainToEntity(producto))
                .map(productoMapper::entityToDomain)
                .map(productoMapper::toResponseDTO)
                .doOnSuccess(response -> log.info("Producto creado: {}", response.getId()))
                .doOnError(error -> log.error("Error al crear producto", error)));
    }

    @Override
    public Mono<ProductoResponseDTO> getById(UUID id) {
        return productoRepository.findById(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Producto no encontrado con ID: " + id)))
                .map(productoMapper::entityToDomain)
                .map(productoMapper::toResponseDTO)
                .doOnSuccess(response -> log.info("Producto obtenido: {}", id))
                .doOnError(error -> log.error("Error al obtener producto", error));
    }

    @Override
    public Flux<ProductoResponseDTO> getAll() {
        return productoRepository.findAll()
                .map(productoMapper::entityToDomain)
                .map(productoMapper::toResponseDTO)
                .doOnComplete(() -> log.info("Se obtuvieron todos los productos"))
                .doOnError(error -> log.error("Error al obtener productos", error));
    }

    @Override
    public Flux<ProductoResponseDTO> getBySucursalId(UUID sucursalId) {
        return productoRepository.findAll()
                .filter(entity -> entity.getSucursalId().equals(sucursalId))
                .map(productoMapper::entityToDomain)
                .map(productoMapper::toResponseDTO)
                .doOnComplete(() -> log.info("Se obtuvieron productos para sucursal: {}", sucursalId))
                .doOnError(error -> log.error("Error al obtener productos por sucursal", error));
    }

    @Override
    public Mono<ProductoResponseDTO> updateStock(UUID id, UpdateStockDTO dto) {
        return productoRepository.findById(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Producto no encontrado con ID: " + id)))
                .flatMap(entity -> {
                    entity.setStock(dto.getStock());
                    entity.setUpdatedAt(Instant.now());
                    return productoRepository.save(entity);
                })
                .map(productoMapper::entityToDomain)
                .map(productoMapper::toResponseDTO)
                .doOnSuccess(response -> log.info("Stock actualizado para producto: {}", id))
                .doOnError(error -> log.error("Error al actualizar stock", error));
    }

    @Override
    public Mono<ProductoResponseDTO> updateNombre(UUID id, UpdateNameDTO dto) {
        return productoRepository.findById(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Producto no encontrado con ID: " + id)))
                .flatMap(entity -> {
                    entity.setNombre(dto.getNombre());
                    entity.setUpdatedAt(Instant.now());
                    return productoRepository.save(entity);
                })
                .map(productoMapper::entityToDomain)
                .map(productoMapper::toResponseDTO)
                .doOnSuccess(response -> log.info("Producto actualizado: {}", id))
                .doOnError(error -> log.error("Error al actualizar producto", error));
    }

    @Override
    public Mono<Void> delete(UUID id) {
        return productoRepository.deleteById(id)
                .doOnSuccess(unused -> log.info("Producto eliminado: {}", id))
                .doOnError(error -> log.error("Error al eliminar producto", error));
    }

    @Override
    public Flux<ProductoMaxStockDTO> getMaxStockByFranquicia(UUID franquiciaId) {
        return sucursalRepository.findAll()
                .filter(sucursal -> sucursal.getFranquiciaId().equals(franquiciaId))
                .flatMap(sucursal -> productoRepository.findAll()
                        .filter(producto -> producto.getSucursalId().equals(sucursal.getId()))
                        .collectList()
                        .flatMapIterable(productos -> {
                            var maxProducto = productos.stream()
                                    .max((p1, p2) -> p1.getStock().compareTo(p2.getStock()));
                            
                            if (maxProducto.isPresent()) {
                                var prod = maxProducto.get();
                                return java.util.Collections.singletonList(
                                    ProductoMaxStockDTO.builder()
                                            .sucursalNombre(sucursal.getNombre())
                                            .productoNombre(prod.getNombre())
                                            .productoDescripcion(prod.getDescripcion())
                                            .productoPrecio(prod.getPrecio())
                                            .stock(prod.getStock())
                                            .build()
                                );
                            }
                            return java.util.Collections.emptyList();
                        })
                )
                .doOnComplete(() -> log.info("Se obtuvieron productos con max stock para franquicia: {}", franquiciaId))
                .doOnError(error -> log.error("Error al obtener max stock por franquicia", error));
    }
}
