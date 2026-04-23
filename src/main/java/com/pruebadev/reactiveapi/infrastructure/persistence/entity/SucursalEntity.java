package com.pruebadev.reactiveapi.infrastructure.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("sucursal")
public class SucursalEntity {

    @Id
    @Column("id")
    private UUID id;
    @Column("franquicia_id")
    private UUID franquiciaId;
    @Column("nombre")
    private String nombre;
    @Column("direccion")
    private String direccion;
    @Column("ciudad")
    private String ciudad;
    @Column("telefono")
    private String telefono;
    @Column("activa")
    private boolean activa;
    @Column("created_at")
    private Instant createdAt;
    @Column("updated_at")
    private Instant updatedAt;
}