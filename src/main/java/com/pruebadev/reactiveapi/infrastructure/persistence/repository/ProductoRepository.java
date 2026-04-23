package com.pruebadev.reactiveapi.infrastructure.persistence.repository;

import com.pruebadev.reactiveapi.infrastructure.persistence.entity.ProductoEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface ProductoRepository extends ReactiveCrudRepository<ProductoEntity, UUID> {
}