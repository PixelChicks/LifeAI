package com.lifeAI.LifeAI.services;

import com.lifeAI.LifeAI.model.DailyProgress;
import com.lifeAI.LifeAI.respository.DailyProgressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DailyProgressService {

    private final DailyProgressRepository dailyProgressRepository;

    @Transactional
    public DailyProgress completeChallenge(Long userId, Integer challengeNumber) {
        if (challengeNumber < 1 || challengeNumber > 5) {
            throw new IllegalArgumentException("Challenge number must be between 1 and 5");
        }

        LocalDate today = LocalDate.now();
        
        DailyProgress progress = dailyProgressRepository
            .findByUserIdAndDate(userId, today)
            .orElse(DailyProgress.builder()
                .userId(userId)
                .date(today)
                .build());
        
        progress.addCompletedChallenge(challengeNumber);
        
        return dailyProgressRepository.save(progress);
    }

    public Optional<DailyProgress> getTodaysProgress(Long userId) {
        return dailyProgressRepository.findByUserIdAndDate(userId, LocalDate.now());
    }
}
