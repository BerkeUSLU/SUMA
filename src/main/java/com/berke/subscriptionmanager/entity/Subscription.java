package com.berke.subscriptionmanager.entity;

import com.berke.subscriptionmanager.entity.user.User;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Table(name="all_subscriptions")

public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="subs_id")
    private long id;

    @ManyToOne
    @NotNull
    @JoinColumn( name = "user_id")
    private User subscriptionUser;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "service_id")
    private ServiceProvider serviceProvider;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate subsBeginDate;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate subsEndDate;

    private String userNameToLogin;

    private String passWordToLogin;

    @NotNull
    private boolean isActive = true;

    public Subscription(User subscriptionUser, ServiceProvider serviceProvider, LocalDate subsBeginDate,
                        LocalDate subsEndDate, String userNameToLogin, String passwordToLogin) {
        this.subscriptionUser = subscriptionUser;
        this.serviceProvider = serviceProvider;
        this.subsBeginDate = subsBeginDate;
        this.subsEndDate = subsEndDate;
        this.userNameToLogin = userNameToLogin;
        this.passWordToLogin = passwordToLogin;
        this.isActive = true;
    }
}
