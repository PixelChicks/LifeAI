package com.lifeAI.LifeAI.services.security;

import com.lifeAI.LifeAI.config.FrontendConfig;
import com.lifeAI.LifeAI.model.User;
import com.lifeAI.LifeAI.services.TokenService;
import com.lifeAI.LifeAI.services.security.events.OnPasswordResetRequestEvent;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Component responsible for listening to password reset request events and sending password reset emails.
 */
@Component
@RequiredArgsConstructor
public class PasswordResetListener implements ApplicationListener<OnPasswordResetRequestEvent> {

    private final JavaMailSender mailSender;
    private final FrontendConfig frontendConfig;
    private final TokenService tokenService;

    /**
     * Sends a password reset email upon receiving the password reset request event.
     */
    @Override
    @Async
    public void onApplicationEvent(@NotNull OnPasswordResetRequestEvent event) {
        sendPasswordResetEmail(event);
    }

    protected void sendPasswordResetEmail(OnPasswordResetRequestEvent event) {
        User user = event.getUser();

        // Generate a password reset token and create a verification token for the user
        String token = generateResetToken(user);
        tokenService.createVerificationToken(user, token);

        String recipientAddress = user.getEmail();
        String subject = "Заявка за нулиране на парола в LIFE";
        String message = getEmailMessage(token, user);

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message);

        mailSender.send(email);
    }

    @NotNull
    private String getEmailMessage(String token, User user) {
        String confirmationUrl = frontendConfig.getForgottenPasswordUrl() + "?token=" + token;

        return "Здравейте, " + user.getUsername() + ",\n\n"
                + "Получихме заявка за нулиране на паролата за вашия акаунт.\n"
                + "Моля, кликнете върху следния линк, за да нулирате паролата си:\n"
                + confirmationUrl + "\n\n"
                + "Ако не сте изпращали такава заявка, моля игнорирайте този имейл.\n\n"
                + "С най-добри пожелания,\n"
                + "Екипът на LIFE!";
    }

    private String generateResetToken(User user) {
        return UUID.randomUUID().toString();
    }
}