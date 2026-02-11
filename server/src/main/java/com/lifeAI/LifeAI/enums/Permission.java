package com.lifeAI.LifeAI.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enumeration representing permissions available in the application.
 * Each permission has a corresponding string representation.
 * This class is used to define and access different permissions required for authorization purposes.
 */
@Getter
@RequiredArgsConstructor
public enum Permission {
    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"),
    ADMIN_DELETE("admin:delete"),
    PATIENT_READ("user:read"),
    PATIENT_UPDATE("user:update"),
    PATIENT_CREATE("user:create"),
    PATIENT_DELETE("user:delete");

    private final String permission;
}

