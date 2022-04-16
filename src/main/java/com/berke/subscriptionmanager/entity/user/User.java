/**
 * User Entity class
 */
package com.berke.subscriptionmanager.entity.user;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Entity
@Data
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@ApiModel(value="User entity", description = "User Entity")

public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="user_id")
    @ApiModelProperty(value="Id field for user model")
    private long id;
    @NotBlank
    @ApiModelProperty(value="Username field for user model")
    private String firstName;
    @NotBlank
    @ApiModelProperty(value="Last name field for user model")
    private String lastName;
    @Email
    @ApiModelProperty(value="Email field for user model")
    @Column(unique = true)
    private String email;
    @NotBlank
    @ApiModelProperty(value="Password field for user model")
    @ToString.Exclude
    private String password;

    @NotBlank
    @Column(unique = true)
    private String userName;

    @Enumerated(EnumType.STRING)
    //@Column(columnDefinition = "varchar(255) default 'USER'") needs to update ddl
    private UserRole userRole = UserRole.USER;

    @Column(name = "enabled")
    private boolean enabled = false;

    /**
     * Construct user object
     * @param firstName user name info
     * @param lastName
     * @param email
     * @param password
     */
    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.userName = email;
        this.enabled = false;

    }
    /**
     * Construct user object
     * @param firstName user name info
     * @param lastName
     * @param email
     * @param password
     */
    public User(String firstName, String lastName, String email, String password, UserRole role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.userName = email;
        this.userRole = role;
        this.enabled = false;

    }
}
