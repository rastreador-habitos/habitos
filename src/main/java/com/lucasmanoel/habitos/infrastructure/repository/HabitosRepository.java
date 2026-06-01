package com.lucasmanoel.habitos.infrastructure.repository;

import com.lucasmanoel.habitos.infrastructure.entity.HabitosEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HabitosRepository extends MongoRepository<HabitosEntity, String> {

}
