package com.berke.subscriptionmanager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class LoginController {


    @GetMapping("/login")
    public String showLoginForm() {
        return "custom_login";
    }
}
