package com.lifeAI.LifeAI.respository;

import com.lifeAI.LifeAI.model.DailyProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface DailyProgressRepository extends JpaRepository<DailyProgress, Long> {
    
    Optional<DailyProgress> findByUserIdAndDate(Long userId, LocalDate date);
}