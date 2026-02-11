package com.lifeAI.LifeAI.model;

import com.lifeAI.LifeAI.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String uuid;

    private String firstName;

    private String lastName;

    @Column(unique = true)
    private String email;

    private String password;
    private Boolean encrypted;
    private String role;
    @Column(name = "enabled")
    private boolean enabled;
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "email_verified_at")
    private LocalDateTime emailVerifiedAt;

    private LocalDateTime deletedAt;

    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role == null) return List.of();

        try {
            Role roleEnum = Role.valueOf(this.role.toUpperCase());
            return roleEnum.getAuthorities();
        } catch (IllegalArgumentException e) {
            return List.of();
        }
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    // Role check helpers
    public boolean isAdmin() {
        return "admin".equalsIgnoreCase(role);
    }

    public boolean isDoctor() {
        return "doctor".equalsIgnoreCase(role);
    }

//    // Password update method example
//    public boolean updatePassword(String currentPassword, String newPassword, String confirmPassword) {
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        if (currentPassword != null && encoder.matches(currentPassword, this.password) && newPassword.equals(confirmPassword)) {
//            this.password = encoder.encode(newPassword);
//            return true;
//        }
//        return false;
//    }

    public boolean isAssistant() {
        return "assistant".equalsIgnoreCase(role);
    }

    public boolean isPatient() {
        return "patient".equalsIgnoreCase(role);
    }
}