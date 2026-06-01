package com.lucasmanoel.habitos.business.converter;

import com.lucasmanoel.habitos.business.dto.HabitosDTORecords;
import com.lucasmanoel.habitos.infrastructure.entity.HabitosEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface HabitosUpdateConverter {
    void updateHabitos(HabitosDTORecords dto, @MappingTarget HabitosEntity entity);
}
