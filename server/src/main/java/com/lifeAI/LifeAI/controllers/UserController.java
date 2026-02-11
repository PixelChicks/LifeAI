package com.lifeAI.LifeAI.controllers;

import com.lifeAI.LifeAI.model.dto.request.UserRequestDTO;
import com.lifeAI.LifeAI.model.dto.request.UserResponseDTO;
import com.lifeAI.LifeAI.model.dto.response.BulkUserResponseDTO;
import com.lifeAI.LifeAI.model.dto.response.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO request) {
        UserResponseDTO response = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/bulk")
    public ResponseEntity<BulkUserResponseDTO> createUsers(@Valid @RequestBody List<UserRequestDTO> requests) {
        BulkUserResponseDTO response = userService.createUsers(requests);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}