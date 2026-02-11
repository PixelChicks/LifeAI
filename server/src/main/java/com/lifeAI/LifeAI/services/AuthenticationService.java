package com.lifeAI.LifeAI.services;

import com.lifeAI.LifeAI.model.User;
import com.lifeAI.LifeAI.model.dto.auth.AuthenticationRequest;
import com.lifeAI.LifeAI.model.dto.auth.AuthenticationResponse;
import com.lifeAI.LifeAI.model.dto.auth.RegisterRequest;

import java.io.IOException;

public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest request);

    AuthenticationResponse authenticate(AuthenticationRequest request);

    AuthenticationResponse refreshToken(String refreshToken) throws IOException;

    AuthenticationResponse me(
            String jwtToken
    );

    void resetPassword(String token, String newPassword);

    void confirmRegistration(String verificationToken);

    User forgotPassword(String email);
}
