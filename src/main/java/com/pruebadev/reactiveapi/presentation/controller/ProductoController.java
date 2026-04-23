package com.pruebadev.reactiveapi.presentation.controller;

import com.pruebadev.reactiveapi.application.dto.CreateProductoDTO;
import com.pruebadev.reactiveapi.application.dto.ProductoMaxStockDTO;
import com.pruebadev.reactiveapi.application.dto.ProductoResponseDTO;
import com.pruebadev.reactiveapi.application.dto.UpdateNameDTO;
import com.pruebadev.reactiveapi.application.dto.UpdateStockDTO;
import com.pruebadev.reactiveapi.application.service.ProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    @PostMapping
    public Mono<ResponseEntity<ProductoResponseDTO>> create(
            @Valid @RequestBody CreateProductoDTO dto) {
        return productoService.create(dto)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response))
                .doOnNext(response -> log.info("Producto creado exitosamente"));
    }

    @GetMapping
    public Flux<ProductoResponseDTO> getAll() {
        return productoService.getAll()
                .doOnComplete(() -> log.info("Se obtuvieron todos los productos"));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<ProductoResponseDTO>> getById(
            @PathVariable UUID id) {
        return productoService.getById(id)
                .map(ResponseEntity::ok)
                .doOnNext(response -> log.info("Producto obtenido: {}", id));
    }

    @GetMapping("/sucursal/{sucursalId}")
    public Flux<ProductoResponseDTO> getBySucursalId(
            @PathVariable UUID sucursalId) {
        return productoService.getBySucursalId(sucursalId)
                .doOnComplete(() -> log.info("Se obtuvieron productos para sucursal: {}", sucursalId));
    }

    @PatchMapping("/{id}/stock")
    public Mono<ResponseEntity<ProductoResponseDTO>> updateStock(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateStockDTO dto) {
        return productoService.updateStock(id, dto)
                .map(ResponseEntity::ok)
                .doOnNext(response -> log.info("Stock actualizado para producto: {}", id));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<ProductoResponseDTO>> updateNombre(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateNameDTO dto) {
        return productoService.updateNombre(id, dto)
                .map(ResponseEntity::ok)
                .doOnNext(response -> log.info("Producto actualizado: {}", id));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(
            @PathVariable UUID id) {
        return productoService.delete(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .doOnNext(response -> log.info("Producto eliminado: {}", id));
    }

    @GetMapping("/franquicia/{franquiciaId}/max-stock")
    public Flux<ProductoMaxStockDTO> getMaxStockByFranquicia(
            @PathVariable UUID franquiciaId) {
        return productoService.getMaxStockByFranquicia(franquiciaId)
                .doOnComplete(() -> log.info("Se obtuvieron productos con max stock para franquicia: {}", franquiciaId));
    }
}
