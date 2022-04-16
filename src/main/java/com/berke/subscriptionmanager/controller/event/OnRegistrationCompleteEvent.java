package com.berke.subscriptionmanager.controller.event;

import com.berke.subscriptionmanager.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;


public class OnRegistrationCompleteEvent extends ApplicationEvent {
    private String appUrl;
    private User user;

    public OnRegistrationCompleteEvent(User user, String appUrl) {
        super(user);

        this.user = user;
        this.appUrl = appUrl;
    }

    public String getAppUrl() {
        return appUrl;
    }



    public User getUser() {
        return user;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }


    public void setUser(User user) {
        this.user = user;
    }
}