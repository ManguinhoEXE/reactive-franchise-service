package com.pruebadev.reactiveapi.infrastructure.mapper;

import com.pruebadev.reactiveapi.application.dto.CreateProductoDTO;
import com.pruebadev.reactiveapi.application.dto.ProductoResponseDTO;
import com.pruebadev.reactiveapi.domain.model.Producto;
import com.pruebadev.reactiveapi.infrastructure.persistence.entity.ProductoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductoMapper {

    ProductoResponseDTO toResponseDTO(Producto producto);

    ProductoResponseDTO entityToResponseDTO(ProductoEntity entity);

    Producto dtoToDomain(CreateProductoDTO dto);

    ProductoEntity domainToEntity(Producto producto);

    Producto entityToDomain(ProductoEntity entity);
}
