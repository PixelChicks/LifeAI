package com.lifeAI.LifeAI.enums;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.lifeAI.LifeAI.enums.Permission.*;


/**
 * Enumeration representing different roles in the application.
 * Each role has a set of associated permissions.
 */
@Getter
@RequiredArgsConstructor
public enum Role {
    PATIENT(Set.of(PATIENT_READ, PATIENT_UPDATE, PATIENT_CREATE, PATIENT_DELETE)),
    ADMIN(Set.of(ADMIN_READ, ADMIN_UPDATE, ADMIN_DELETE, ADMIN_CREATE,
            PATIENT_READ, PATIENT_UPDATE, PATIENT_CREATE, PATIENT_DELETE)),
    DOCTOR(Set.of(/* doctor permissions here */)),
    ASSISTANT(Set.of(/* assistant permissions here */));

    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = permissions.stream()
                .map(p -> new SimpleGrantedAuthority(p.getPermission()))
                .collect(Collectors.toList());

        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name().toUpperCase()));
        return authorities;
    }
}