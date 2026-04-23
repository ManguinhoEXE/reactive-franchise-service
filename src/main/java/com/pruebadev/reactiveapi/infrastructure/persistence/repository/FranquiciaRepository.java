package com.pruebadev.reactiveapi.infrastructure.persistence.repository;

import com.pruebadev.reactiveapi.infrastructure.persistence.entity.FranquiciaEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface FranquiciaRepository extends ReactiveCrudRepository<FranquiciaEntity, UUID> {
}