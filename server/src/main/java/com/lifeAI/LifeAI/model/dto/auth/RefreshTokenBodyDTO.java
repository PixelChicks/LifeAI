package com.lifeAI.LifeAI.model.dto.auth;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenBodyDTO {
    private String refreshToken;
}
