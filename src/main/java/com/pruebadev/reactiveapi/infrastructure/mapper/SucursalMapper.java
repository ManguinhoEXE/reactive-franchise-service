package com.pruebadev.reactiveapi.infrastructure.mapper;

import com.pruebadev.reactiveapi.application.dto.CreateSucursalDTO;
import com.pruebadev.reactiveapi.application.dto.SucursalResponseDTO;
import com.pruebadev.reactiveapi.domain.model.Sucursal;
import com.pruebadev.reactiveapi.infrastructure.persistence.entity.SucursalEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SucursalMapper {

    SucursalResponseDTO toResponseDTO(Sucursal sucursal);

    SucursalResponseDTO entityToResponseDTO(SucursalEntity entity);

    Sucursal dtoToDomain(CreateSucursalDTO dto);

    SucursalEntity domainToEntity(Sucursal sucursal);

    Sucursal entityToDomain(SucursalEntity entity);
}
