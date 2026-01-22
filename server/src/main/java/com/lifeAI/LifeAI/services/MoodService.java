package com.lifeAI.LifeAI.services;

import com.lifeAI.LifeAI.model.MoodEntry;
import com.lifeAI.LifeAI.respository.MoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MoodService {

    private final MoodRepository moodRepository;

    @Transactional
    public MoodEntry saveMood(Long userId, Integer moodLevel) {
        LocalDate today = LocalDate.now();
        
        // Check if mood already exists for today
        Optional<MoodEntry> existing = moodRepository.findByUserIdAndDate(userId, today);
        
        if (existing.isPresent()) {
            // Update existing mood
            MoodEntry mood = existing.get();
            mood.setMoodLevel(moodLevel);
            return moodRepository.save(mood);
        } else {
            // Create new mood entry
            MoodEntry mood = MoodEntry.builder()
                .userId(userId)
                .moodLevel(moodLevel)
                .date(today)
                .build();
            return moodRepository.save(mood);
        }
    }

    public Optional<MoodEntry> getTodaysMood(Long userId) {
        return moodRepository.findByUserIdAndDate(userId, LocalDate.now());
    }
}