package com.lucasmanoel.habitos.infrasctructure.repository;

import com.lucasmanoel.habitos.infrasctructure.entity.CheckinEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CheckinRepository extends MongoRepository<CheckinEntity, String> {
    List<CheckinEntity> findByHabitosIDAndDataAfter(String habitoId, LocalDate data);
    List<CheckinEntity> findByHabitosID(String habitoId);
    boolean existsByHabitosIDAndData(String habitoId, LocalDate data);
    void deleteByHabitosID(String habitosID);
}
