package com.pruebadev.reactiveapi.presentation.controller;

import com.pruebadev.reactiveapi.application.dto.CreateSucursalDTO;
import com.pruebadev.reactiveapi.application.dto.SucursalResponseDTO;
import com.pruebadev.reactiveapi.application.dto.UpdateNameDTO;
import com.pruebadev.reactiveapi.application.service.SucursalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("/api/sucursales")
@RequiredArgsConstructor
public class SucursalController {

    private final SucursalService sucursalService;

    @PostMapping
    public Mono<ResponseEntity<SucursalResponseDTO>> create(
            @Valid @RequestBody CreateSucursalDTO dto) {
        return sucursalService.create(dto)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response))
                .doOnNext(response -> log.info("Sucursal creada exitosamente"));
    }

    @GetMapping
    public Flux<SucursalResponseDTO> getAll() {
        return sucursalService.getAll()
                .doOnComplete(() -> log.info("Se obtuvieron todas las sucursales"));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<SucursalResponseDTO>> getById(
            @PathVariable UUID id) {
        return sucursalService.getById(id)
                .map(ResponseEntity::ok)
                .doOnNext(response -> log.info("Sucursal obtenida: {}", id));
    }

    @GetMapping("/franquicia/{franquiciaId}")
    public Flux<SucursalResponseDTO> getByFranquiciaId(
            @PathVariable UUID franquiciaId) {
        return sucursalService.getByFranquiciaId(franquiciaId)
                .doOnComplete(() -> log.info("Se obtuvieron sucursales para franquicia: {}", franquiciaId));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<SucursalResponseDTO>> updateNombre(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateNameDTO dto) {
        return sucursalService.updateNombre(id, dto)
                .map(ResponseEntity::ok)
                .doOnNext(response -> log.info("Sucursal actualizada: {}", id));
    }
}
