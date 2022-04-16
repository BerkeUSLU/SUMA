package com.berke.subscriptionmanager.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceProviderDto {
    private int id;
    private int providerId;
    private String membershipPlan;
    private double monthlyFee;
}
