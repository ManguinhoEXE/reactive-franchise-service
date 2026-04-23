package com.pruebadev.reactiveapi.infrastructure.mapper;

import com.pruebadev.reactiveapi.application.dto.CreateFranquiciaDTO;
import com.pruebadev.reactiveapi.application.dto.FranquiciaResponseDTO;
import com.pruebadev.reactiveapi.domain.model.Franquicia;
import com.pruebadev.reactiveapi.infrastructure.persistence.entity.FranquiciaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface FranquiciaMapper {

    FranquiciaResponseDTO toResponseDTO(Franquicia franquicia);

    FranquiciaResponseDTO entityToResponseDTO(FranquiciaEntity entity);

    Franquicia dtoToDomain(CreateFranquiciaDTO dto);

    FranquiciaEntity domainToEntity(Franquicia franquicia);

    Franquicia entityToDomain(FranquiciaEntity entity);
}
