package com.lucasmanoel.habitos.business.mapper;

import com.lucasmanoel.habitos.business.dto.HabitosDTORecords;
import com.lucasmanoel.habitos.infrasctructure.entity.HabitosEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HabitosConverter {

    HabitosDTORecords paraHabitosDTO(HabitosEntity entity);
}
