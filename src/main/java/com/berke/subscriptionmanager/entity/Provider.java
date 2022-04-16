package com.berke.subscriptionmanager.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Generated;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Data
@NoArgsConstructor
public class Provider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="provider_id")
    private int id;
    @NotBlank
    private String providerName;

    public Provider(String providerName) {
        this.providerName = providerName;
    }
}
