package com.lifeAI.LifeAI.services;

import com.lifeAI.LifeAI.model.dto.auth.AuthenticationResponse;

public interface OAuth2AuthenticationService {

    String getOAuthGoogleLoginUrl();

    AuthenticationResponse processOAuthGoogleLogin(String code);
}
