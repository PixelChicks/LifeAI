package com.lifeAI.LifeAI.services.security;

import com.lifeAI.LifeAI.model.User;
import com.lifeAI.LifeAI.services.TokenService;
import com.lifeAI.LifeAI.services.security.events.OnRegistrationCompleteEvent;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Component responsible for handling registration confirmation emails.
 */
@Component
@RequiredArgsConstructor
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {
    private final TokenService tokenService;
    private final MessageSource messages;
    private final JavaMailSender mailSender;

    @Override
    @Async
    public void onApplicationEvent(@NotNull OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    protected void confirmRegistration(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        tokenService.createVerificationToken(user, token);

        String recipientAddress = user.getEmail();
        String subject = "Потвърждение на регистрацията в LIFE";
        String message = getEmailNote(event, token, user);

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message);

        mailSender.send(email);
    }

    private static @NotNull String getEmailNote(OnRegistrationCompleteEvent event, String token, User user) {
        String confirmationUrl = event.getAppUrl() + "auth/registrationConfirm?token=" + token;

        return "Здравейте, " + user.getUsername() + "\n\n"
                + "Благодарим Ви, че се регистрирахте в LIFE!\n\n"
                + "За да завършите регистрацията си, моля кликнете върху следния линк, за да потвърдите вашия имейл:\n"
                + confirmationUrl + "\n\n"
                + "Ако не сте създали акаунт при нас, моля игнорирайте този имейл.\n\n"
                + "С най-добри пожелания,\n"
                + "Екипът на LIFE!";
    }
}
