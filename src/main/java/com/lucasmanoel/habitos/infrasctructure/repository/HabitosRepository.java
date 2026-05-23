package com.lucasmanoel.habitos.infrasctructure.repository;

import com.lucasmanoel.habitos.infrasctructure.entity.HabitosEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HabitosRepository extends MongoRepository<HabitosEntity, String> {

}
