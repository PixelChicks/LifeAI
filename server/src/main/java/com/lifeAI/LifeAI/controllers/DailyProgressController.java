package com.lifeAI.LifeAI.controllers;

import com.lifeAI.LifeAI.model.DailyProgress;
import com.lifeAI.LifeAI.model.User;
import com.lifeAI.LifeAI.services.DailyProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/progress")
@RequiredArgsConstructor
public class DailyProgressController {

    private final DailyProgressService dailyProgressService;

    @PostMapping("/complete")
    public ResponseEntity<DailyProgress> completeChallenge(
            @AuthenticationPrincipal User user,
            @RequestBody Map<String, Integer> request
    ) {
        Integer challengeNumber = request.get("challengeNumber");
        
        if (challengeNumber == null || challengeNumber < 1 || challengeNumber > 5) {
            return ResponseEntity.badRequest().build();
        }

        DailyProgress progress = dailyProgressService.completeChallenge(
            user.getId(), 
            challengeNumber
        );
        
        return ResponseEntity.ok(progress);
    }

    @GetMapping("/today")
    public ResponseEntity<DailyProgress> getTodaysProgress(
            @AuthenticationPrincipal User user
    ) {
        return dailyProgressService.getTodaysProgress(user.getId())
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.ok(DailyProgress.builder()
                .userId(user.getId())
                .build()));
    }
}