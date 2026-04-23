package com.pruebadev.reactiveapi.presentation.controller;

import com.pruebadev.reactiveapi.application.dto.CreateFranquiciaDTO;
import com.pruebadev.reactiveapi.application.dto.FranquiciaResponseDTO;
import com.pruebadev.reactiveapi.application.dto.UpdateNameDTO;
import com.pruebadev.reactiveapi.application.service.FranquiciaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping("/api/franquicias")
@RequiredArgsConstructor
public class FranquiciaController {

    private final FranquiciaService franquiciaService;

    @PostMapping
    public Mono<ResponseEntity<FranquiciaResponseDTO>> create(
            @Valid @RequestBody CreateFranquiciaDTO dto) {
        return franquiciaService.create(dto)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response))
                .doOnNext(response -> log.info("Franquicia creada exitosamente"));
    }

    @GetMapping
    public Flux<FranquiciaResponseDTO> getAll() {
        return franquiciaService.getAll()
                .doOnComplete(() -> log.info("Se obtuvieron todas las franquicias"));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<FranquiciaResponseDTO>> getById(
            @PathVariable UUID id) {
        return franquiciaService.getById(id)
                .map(ResponseEntity::ok)
                .doOnNext(response -> log.info("Franquicia obtenida: {}", id));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<FranquiciaResponseDTO>> updateNombre(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateNameDTO dto) {
        return franquiciaService.updateNombre(id, dto)
                .map(ResponseEntity::ok)
                .doOnNext(response -> log.info("Franquicia actualizada: {}", id));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(
            @PathVariable UUID id) {
        return franquiciaService.delete(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .doOnNext(response -> log.info("Franquicia eliminada: {}", id));
    }
}
