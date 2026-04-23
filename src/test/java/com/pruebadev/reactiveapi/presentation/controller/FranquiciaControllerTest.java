package com.pruebadev.reactiveapi.presentation.controller;

import com.pruebadev.reactiveapi.application.dto.CreateFranquiciaDTO;
import com.pruebadev.reactiveapi.application.dto.FranquiciaResponseDTO;
import com.pruebadev.reactiveapi.application.service.FranquiciaService;
import com.pruebadev.reactiveapi.infrastructure.exception.EntityNotFoundException;
import com.pruebadev.reactiveapi.infrastructure.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FranquiciaControllerTest {

    private WebTestClient webTestClient;

    @InjectMocks
    private FranquiciaController controller;

    @Mock
    private FranquiciaService franquiciaService;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToController(controller)
                .controllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void create_shouldReturn201() {
        var response = FranquiciaResponseDTO.builder().id(UUID.randomUUID()).nombre("Franquicia 1").codigo("F1").activa(true).build();
        when(franquiciaService.create(any(CreateFranquiciaDTO.class))).thenReturn(Mono.just(response));

        webTestClient.post()
                .uri("/api/franquicias")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"nombre\":\"Franquicia 1\",\"codigo\":\"F1\",\"descripcion\":\"Desc\"}")
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.nombre").isEqualTo("Franquicia 1");
    }

    @Test
    void create_whenInvalid_shouldReturn400() {
        webTestClient.post()
                .uri("/api/franquicias")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"nombre\":\"\",\"codigo\":\"\"}")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void getById_whenMissing_shouldReturn404() {
        UUID id = UUID.randomUUID();
        when(franquiciaService.getById(id)).thenReturn(Mono.error(new EntityNotFoundException("No existe")));

        webTestClient.get()
                .uri("/api/franquicias/{id}", id)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.error").isEqualTo("NOT_FOUND");
    }

    @Test
    void reactiveFlow_getAll_shouldBeValidatedWithStepVerifier() {
        var one = FranquiciaResponseDTO.builder().id(UUID.randomUUID()).nombre("A").codigo("A1").activa(true).build();
        var two = FranquiciaResponseDTO.builder().id(UUID.randomUUID()).nombre("B").codigo("B1").activa(true).build();
        when(franquiciaService.getAll()).thenReturn(Flux.just(one, two));

        StepVerifier.create(controller.getAll())
                .expectNextMatches(item -> "A".equals(item.getNombre()))
                .expectNextMatches(item -> "B".equals(item.getNombre()))
                .verifyComplete();
    }

    @Test
    void reactiveFlow_getById_shouldBeValidatedWithStepVerifier() {
        UUID id = UUID.randomUUID();
        var dto = FranquiciaResponseDTO.builder().id(id).nombre("A").codigo("A1").activa(true).build();
        when(franquiciaService.getById(id)).thenReturn(Mono.just(dto));

        StepVerifier.create(controller.getById(id))
                .expectNextMatches(response -> response.getStatusCode().is2xxSuccessful()
                        && response.getBody() != null
                        && id.equals(response.getBody().getId()))
                .verifyComplete();
    }
}
