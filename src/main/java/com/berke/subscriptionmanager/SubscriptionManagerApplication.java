package com.berke.subscriptionmanager;


import com.berke.subscriptionmanager.service.EmailServiceImpl;
import com.berke.subscriptionmanager.entity.user.User;
import com.berke.subscriptionmanager.entity.user.UserRole;
import com.berke.subscriptionmanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class SubscriptionManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SubscriptionManagerApplication.class, args);
    }

    @Autowired
    private UserService userService;

    @Autowired
    private EmailServiceImpl emailService;

    @EventListener(ApplicationReadyEvent.class)
    public void execAfterStartCreateAdminIfNotExist() {
        if (userService.getUserByUsername("admin").isEmpty()) {
            User admin = new User("Berke", "USLU", "admin", "1234", UserRole.ADMIN);
            admin.setEnabled(true);
            userService.saveNewUser(admin);
        }
        if (userService.getUserByUsername("user").isEmpty()) {
            User user = new User("Berke", "USLU", "user", "1234", UserRole.USER);
            user.setEnabled(true);
            userService.saveNewUser(user);
        }

    }
}
