package com.berke.subscriptionmanager.controller;

import com.berke.subscriptionmanager.entity.user.CustomUserDetails;
import com.berke.subscriptionmanager.entity.user.User;
import com.berke.subscriptionmanager.entity.user.UserRole;
import com.berke.subscriptionmanager.service.CustomUserDetailsService;
import com.berke.subscriptionmanager.service.UserService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@Log
public class AuthHelper {
    @Autowired
    private UserService userService;

    public boolean hasRole(UserRole role) {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(role.name()));
    }

    public boolean isLogginUser(Long id) {
        return id == userService.getCurrentUser().getId();
    }


}
