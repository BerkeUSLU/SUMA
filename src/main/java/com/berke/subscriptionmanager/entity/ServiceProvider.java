package com.berke.subscriptionmanager.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class ServiceProvider {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="service_id")
    private int id;
    @ManyToOne
    @JoinColumn(name = "provider_id")
    @NotBlank
    private Provider provider;

    @NotBlank
    private String membershipPlan;

    @NotBlank
    private double monthlyFee;
}