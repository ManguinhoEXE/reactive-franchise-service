package com.pruebadev.reactiveapi.presentation.controller;

import com.pruebadev.reactiveapi.application.dto.ProductoResponseDTO;
import com.pruebadev.reactiveapi.application.service.ProductoService;
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
class ProductoControllerTest {

    private WebTestClient webTestClient;

    @InjectMocks
    private ProductoController controller;

    @Mock
    private ProductoService productoService;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToController(controller)
                .controllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void patchStock_whenValid_shouldReturn200() {
        UUID id = UUID.randomUUID();
        var response = ProductoResponseDTO.builder().id(id).nombre("Prod").stock(10).build();
        when(productoService.updateStock(any(UUID.class), any())).thenReturn(Mono.just(response));

        webTestClient.patch()
                .uri("/api/productos/{id}/stock", id)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"stock\":10}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.stock").isEqualTo(10);
    }

    @Test
    void patchStock_whenInvalid_shouldReturn400() {
        UUID id = UUID.randomUUID();

        webTestClient.patch()
                .uri("/api/productos/{id}/stock", id)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"stock\":-1}")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void getById_whenMissing_shouldReturn404() {
        UUID id = UUID.randomUUID();
        when(productoService.getById(id)).thenReturn(Mono.error(new EntityNotFoundException("Producto no encontrado")));

        webTestClient.get()
                .uri("/api/productos/{id}", id)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.error").isEqualTo("NOT_FOUND");
    }

    @Test
    void reactiveFlow_getById_shouldBeValidatedWithStepVerifier() {
        UUID id = UUID.randomUUID();
        var dto = ProductoResponseDTO.builder().id(id).nombre("Prod").stock(3).build();
        when(productoService.getById(id)).thenReturn(Mono.just(dto));

        StepVerifier.create(controller.getById(id))
                .expectNextMatches(response -> response.getBody() != null && "Prod".equals(response.getBody().getNombre()))
                .verifyComplete();
    }
}
