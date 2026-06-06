package com.lucasmanoel.habitos.business.converter;

import com.lucasmanoel.habitos.business.dto.HabitosDTORecords;
import com.lucasmanoel.habitos.business.dto.HabitosDTOResponse;
import com.lucasmanoel.habitos.infrastructure.entity.HabitosEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HabitosConverter {

    HabitosDTORecords paraHabitosDTO(HabitosEntity entity);

    @Mapping(source = "ativo", target = "status")
    HabitosDTOResponse paraHabitosDTOResponse(HabitosEntity entity);

    List<HabitosDTOResponse> paraListaDTOResponse(List<HabitosEntity> habitosEntity);

}
