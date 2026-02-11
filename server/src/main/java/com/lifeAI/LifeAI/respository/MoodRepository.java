package com.lifeAI.LifeAI.respository;

import com.lifeAI.LifeAI.model.MoodEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface MoodRepository extends JpaRepository<MoodEntry, Long> {
    
    Optional<MoodEntry> findByUserIdAndDate(Long userId, LocalDate date);
    
    boolean existsByUserIdAndDate(Long userId, LocalDate date);
}