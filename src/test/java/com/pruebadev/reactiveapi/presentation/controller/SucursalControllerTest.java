package com.pruebadev.reactiveapi.presentation.controller;

import com.pruebadev.reactiveapi.application.dto.SucursalResponseDTO;
import com.pruebadev.reactiveapi.application.service.SucursalService;
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
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SucursalControllerTest {

    private WebTestClient webTestClient;

    @InjectMocks
    private SucursalController controller;

    @Mock
    private SucursalService sucursalService;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToController(controller)
                .controllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void create_whenValid_shouldReturn201() {
        var response = SucursalResponseDTO.builder().id(UUID.randomUUID()).nombre("Centro").build();
        when(sucursalService.create(any())).thenReturn(Mono.just(response));

        webTestClient.post()
                .uri("/api/sucursales")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"franquiciaId\":\"" + UUID.randomUUID() + "\",\"nombre\":\"Centro\",\"direccion\":\"Calle 1\",\"ciudad\":\"Bogota\"}")
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.nombre").isEqualTo("Centro");
    }

    @Test
    void create_whenInvalid_shouldReturn400() {
        webTestClient.post()
                .uri("/api/sucursales")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"franquiciaId\":null,\"nombre\":\"\",\"direccion\":\"x\",\"ciudad\":\"\"}")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void getById_whenMissing_shouldReturn404() {
        UUID id = UUID.randomUUID();
        when(sucursalService.getById(id)).thenReturn(Mono.error(new EntityNotFoundException("Sucursal no encontrada")));

        webTestClient.get()
                .uri("/api/sucursales/{id}", id)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void reactiveFlow_getById_shouldBeValidatedWithStepVerifier() {
        UUID id = UUID.randomUUID();
        var dto = SucursalResponseDTO.builder().id(id).nombre("Centro").build();
        when(sucursalService.getById(id)).thenReturn(Mono.just(dto));

        StepVerifier.create(controller.getById(id))
                .expectNextMatches(response -> response.getBody() != null && id.equals(response.getBody().getId()))
                .verifyComplete();
    }
}
