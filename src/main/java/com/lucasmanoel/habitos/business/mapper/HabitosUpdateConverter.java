package com.lucasmanoel.habitos.business.mapper;

import com.lucasmanoel.habitos.business.dto.HabitosDTORecords;
import com.lucasmanoel.habitos.infrasctructure.entity.HabitosEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface HabitosUpdateConverter {
    void updateHabitos(HabitosDTORecords dto, @MappingTarget HabitosEntity entity);
}
