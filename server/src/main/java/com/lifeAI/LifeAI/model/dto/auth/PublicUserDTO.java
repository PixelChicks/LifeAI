package com.lifeAI.LifeAI.model.dto.auth;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublicUserDTO {
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String role;
}
