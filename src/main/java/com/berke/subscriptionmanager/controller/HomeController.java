package com.berke.subscriptionmanager.controller;

import com.berke.subscriptionmanager.entity.Subscription;
import com.berke.subscriptionmanager.service.SubscriptionService;
import com.berke.subscriptionmanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private SubscriptionService subscriptionService;


    @Autowired
    private UserService userService;


    @GetMapping({"/", "index", "index.html"})
    public String homePage() {
        return "index";
    }

    @GetMapping("/dashboard")
    public String dashBoard(Model model) {
        List<Subscription> subscriptionList = subscriptionService.getAllSubscriptions(userService.getCurrentUser());

        model.addAttribute("subscription", subscriptionList);

        return "dashboard";
    }

    @GetMapping({"/contact"})
    public String contactPage() {
        return "contact";
    }

}
