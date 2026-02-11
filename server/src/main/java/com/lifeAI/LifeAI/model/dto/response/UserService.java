package com.lifeAI.LifeAI.model.dto.response;

import com.lifeAI.LifeAI.model.User;
import com.lifeAI.LifeAI.model.dto.request.UserRequestDTO;
import com.lifeAI.LifeAI.model.dto.request.UserResponseDTO;
import com.lifeAI.LifeAI.respository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public BulkUserResponseDTO createUsers(List<UserRequestDTO> requests) {
        List<UserResponseDTO> created = new ArrayList<>();
        List<String> failed = new ArrayList<>();

        for (UserRequestDTO request : requests) {
            try {
                created.add(createUser(request));
            } catch (Exception e) {
                failed.add(request.getEmail() + ": " + e.getMessage());
            }
        }

        return BulkUserResponseDTO.builder()
                .successCount(created.size())
                .failureCount(failed.size())
                .createdUsers(created)
                .errors(failed)
                .build();
    }

    public UserResponseDTO createUser(UserRequestDTO request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already in use: " + request.getEmail());
        }

        User user = User.builder()
                .uuid(UUID.randomUUID().toString())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(request.getPassword()) //already crypted from bbca db
                .role(request.getRole().toUpperCase())
                .encrypted(true)
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        User saved = userRepository.save(user);

        return UserResponseDTO.builder()
                .id(saved.getId())
                .uuid(saved.getUuid())
                .firstName(saved.getFirstName())
                .lastName(saved.getLastName())
                .email(saved.getEmail())
                .role(saved.getRole())
                .enabled(saved.isEnabled())
                .createdAt(saved.getCreatedAt())
                .build();
    }
}