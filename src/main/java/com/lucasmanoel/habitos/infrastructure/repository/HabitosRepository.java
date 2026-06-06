package com.lucasmanoel.habitos.infrastructure.repository;

import com.lucasmanoel.habitos.infrastructure.entity.HabitosEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HabitosRepository extends MongoRepository<HabitosEntity, String> {
    List<HabitosEntity> findByEmail(String email);
}
