package com.pruebadev.reactiveapi.infrastructure.persistence.repository;

import com.pruebadev.reactiveapi.infrastructure.persistence.entity.SucursalEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface SucursalRepository extends ReactiveCrudRepository<SucursalEntity, UUID> {
}