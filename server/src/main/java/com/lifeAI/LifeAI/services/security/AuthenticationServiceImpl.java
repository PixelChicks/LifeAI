package com.lifeAI.LifeAI.services.security;

import com.lifeAI.LifeAI.config.rateLimiting.RateLimiterConfigProperties;
import com.lifeAI.LifeAI.enums.TokenType;
import com.lifeAI.LifeAI.exceptions.email.EmailNotVerified;
import com.lifeAI.LifeAI.exceptions.token.ExpiredTokenException;
import com.lifeAI.LifeAI.exceptions.token.InvalidTokenException;
import com.lifeAI.LifeAI.exceptions.user.UserLoginException;
import com.lifeAI.LifeAI.exceptions.user.UserNotFoundException;
import com.lifeAI.LifeAI.model.Token;
import com.lifeAI.LifeAI.model.User;
import com.lifeAI.LifeAI.model.VerificationToken;
import com.lifeAI.LifeAI.model.dto.auth.AuthenticationRequest;
import com.lifeAI.LifeAI.model.dto.auth.AuthenticationResponse;
import com.lifeAI.LifeAI.model.dto.auth.PublicUserDTO;
import com.lifeAI.LifeAI.model.dto.auth.RegisterRequest;
import com.lifeAI.LifeAI.respository.UserRepository;
import com.lifeAI.LifeAI.respository.VerificationTokenRepository;
import com.lifeAI.LifeAI.services.AuthenticationService;
import com.lifeAI.LifeAI.services.JwtService;
import com.lifeAI.LifeAI.services.TokenService;
import com.lifeAI.LifeAI.services.UserService;
import com.lifeAI.LifeAI.utils.PasswordEncryptionUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserService userService;
    private final TokenService tokenService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ModelMapper modelMapper;
    private final VerificationTokenRepository verificationTokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RateLimiterConfigProperties rateLimiterConfigProperties;

    /**
     * Registers a new user based on the provided registration request.
     */
    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        User user = userService.createUser(request);

        userRepository.save(user);

        return tokenService.generateAuthResponse(user);
    }

    // Login with correct email and password
    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        User user;

        try {
            user = userService.findByEmail(request.getEmail());
        } catch (UserNotFoundException userNotFoundException) {
            throw new UserLoginException();
        }

        boolean passedFirstCheck = PasswordEncryptionUtils.validatePassword(request.getPassword(), user.getPassword());

        if (passedFirstCheck) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            userRepository.save(user);

            if (!user.isEnabled()) {
                throw new EmailNotVerified();
            }
        } else {
            try {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword()
                        )
                );
            } catch (DisabledException exception) {
                throw new EmailNotVerified();
            } catch (AuthenticationException exception) {
                throw new UserLoginException();
            }
        }

        System.out.println(user.getEmail());
        tokenService.revokeAllUserTokens(user);
        return tokenService.generateAuthResponse(user);
    }

    /**
     * Generates a new access token and updates the refresh token based on the provided refresh token.
     * If the refresh token is missing or invalid, it throws an InvalidTokenException.
     * If the refresh token is valid, it generates a new access token, revokes all existing user tokens,
     * and updates the refresh token to the provided one.
     */
    @Override
    public AuthenticationResponse refreshToken(String refreshToken) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new InvalidTokenException();
        }

        String userEmail;

        try {
            userEmail = jwtService.extractUsername(refreshToken);
        } catch (JwtException exception) {
            throw new InvalidTokenException();
        }

        if (userEmail == null) {
            throw new InvalidTokenException();
        }

        // Make sure token is a refresh token not access token
        Token token = tokenService.findByToken(refreshToken);
        if (token != null && token.tokenType != TokenType.REFRESH) {
            throw new InvalidTokenException();
        }

        User user = userService.findByEmail(userEmail);

        if (!jwtService.isTokenValid(refreshToken, user)) {
            tokenService.revokeToken(token);
            throw new InvalidTokenException();
        }

        String accessToken = jwtService.generateToken(user);

        tokenService.revokeAllUserTokens(user);
        tokenService.saveToken(user, accessToken, TokenType.ACCESS);
        tokenService.saveToken(user, refreshToken, TokenType.REFRESH);

        return AuthenticationResponse
                .builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * Retrieves user information based on the provided JWT token.
     * If the token is invalid or missing, it throws an InvalidTokenException.
     * If the token is valid, it retrieves the user's access and refresh tokens, updates the refresh token if necessary,
     * and returns an authentication response containing the user's information and tokens.
     */
    @Override
    public AuthenticationResponse me(String jwtToken) {
        if (jwtToken == null || jwtToken.isEmpty()) {
            throw new InvalidTokenException();
        }

        Token accessToken = tokenService.findByToken(jwtToken);

        if (accessToken == null) {
            throw new InvalidTokenException();
        }

        User user = accessToken.getUser();

        boolean isTokenValid;

        try {
            isTokenValid = jwtService.isTokenValid(accessToken.getToken(), user);
        } catch (JwtException jwtException) {
            isTokenValid = false;
        }

        if (!isTokenValid) {
            tokenService.revokeAllUserTokens(user);
            throw new InvalidTokenException();
        }

        List<Token> tokens = tokenService.findByUser(user);
        List<Token> refreshTokens = tokens.stream().filter(x -> x.getTokenType() == TokenType.REFRESH).toList();

        if (refreshTokens.isEmpty()) {
            throw new InvalidTokenException();
        }

        Token refreshToken = refreshTokens.get(0);

        if (refreshToken == null) {
            throw new InvalidTokenException();
        }

        String refreshTokenString;

        if (!jwtService.isTokenValid(refreshToken.getToken(), user)) {
            refreshTokenString = jwtService.generateRefreshToken(user);
            tokenService.saveToken(user, refreshTokenString, TokenType.REFRESH);
        } else {
            refreshTokenString = refreshToken.getToken();
        }

        PublicUserDTO publicUser = modelMapper.map(accessToken.getUser(), PublicUserDTO.class);

        return AuthenticationResponse
                .builder()
                .accessToken(accessToken.getToken())
                .refreshToken(refreshTokenString)
                .user(publicUser)
                .build();
    }

    /**
     * Resets the password for a user based on the provided token and new password.
     */
    public void resetPassword(String token, String newPassword) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if (verificationToken == null) {
            throw new InvalidTokenException();
        }


        User user = verificationToken.getUser();
        if (user == null) {
            throw new InvalidTokenException();
        }

        verificationToken.setCreatedAt(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(newPassword));

        userRepository.save(user);
        verificationTokenRepository.delete(verificationToken);
    }

    @Override
    public void confirmRegistration(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if (verificationToken == null) {
            throw new ExpiredTokenException();
        }

        verificationToken.setCreatedAt(LocalDateTime.now());

        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            throw new ExpiredTokenException();
        }

        User user = verificationToken.getUser();
        user.setEnabled(true);
        user.setEmailVerifiedAt(LocalDateTime.now());

        userRepository.save(user);
        verificationTokenRepository.delete(verificationToken);
    }

    @Override
    public User forgotPassword(String email) {
        User user = userService.findByEmail(email);

        if (!user.isEnabled()) {
            throw new EmailNotVerified();
        }

        return user;
    }
}
