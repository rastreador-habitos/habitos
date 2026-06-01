package com.lucasmanoel.habitos.business.converter;

import com.lucasmanoel.habitos.business.dto.HabitosDTORecords;
import com.lucasmanoel.habitos.infrastructure.entity.HabitosEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HabitosConverter {

    HabitosDTORecords paraHabitosDTO(HabitosEntity entity);
}
