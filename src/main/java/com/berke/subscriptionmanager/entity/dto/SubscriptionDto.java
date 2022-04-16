package com.berke.subscriptionmanager.entity.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class SubscriptionDto {
    private long id;
    private int providerId;
    private String providerName;
    private String membershipPlan;
    private double montlyFee;

    private String usernameToLogin;
    private String passwordToLogin;


    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDate beginDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDate endDate;
}
