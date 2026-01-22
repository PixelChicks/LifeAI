package com.lifeAI.LifeAI.controllers;

import com.lifeAI.LifeAI.model.MoodEntry;
import com.lifeAI.LifeAI.model.User;
import com.lifeAI.LifeAI.services.MoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/mood")
@RequiredArgsConstructor
public class MoodController {

    private final MoodService moodService;

    @PostMapping
    public ResponseEntity<MoodEntry> saveMood(
            @AuthenticationPrincipal User user,
            @RequestBody Map<String, Integer> request
    ) {
        Integer moodLevel = request.get("moodLevel");

        if (moodLevel == null || moodLevel < 1 || moodLevel > 5) {
            return ResponseEntity.badRequest().build();
        }

        MoodEntry saved = moodService.saveMood(user.getId(), moodLevel);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/today")
    public ResponseEntity<MoodEntry> getTodaysMood(@AuthenticationPrincipal User user) {
        return moodService.getTodaysMood(user.getId())
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}