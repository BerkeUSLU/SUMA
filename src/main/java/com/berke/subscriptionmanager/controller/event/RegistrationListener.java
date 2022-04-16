package com.berke.subscriptionmanager.controller.event;

import com.berke.subscriptionmanager.entity.user.User;
import com.berke.subscriptionmanager.service.EmailServiceImpl;
import com.berke.subscriptionmanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    @Autowired
    private UserService userService;


    @Autowired
    private EmailServiceImpl mailSender;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.createVerificationToken(user, token);

        String confirmationUrl
                = event.getAppUrl() + "/auth/confirmRegistration?token=" + token;



        mailSender.sendMessage(user.getEmail(), "Registaration Confirmation",
                 "Follow link to confirm your registration " + "\r\n" + "http://localhost:9001" + confirmationUrl);

    }
}
