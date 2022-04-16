package com.berke.subscriptionmanager.controller;

import com.berke.subscriptionmanager.entity.Provider;
import com.berke.subscriptionmanager.entity.ServiceProvider;
import com.berke.subscriptionmanager.entity.Subscription;
import com.berke.subscriptionmanager.entity.dto.SubscriptionDto;
import com.berke.subscriptionmanager.entity.user.CustomUserDetails;
import com.berke.subscriptionmanager.entity.user.User;
import com.berke.subscriptionmanager.exception.ServiceProviderException;
import com.berke.subscriptionmanager.exception.SubscriptionException;
import com.berke.subscriptionmanager.service.CustomUserDetailsService;
import com.berke.subscriptionmanager.service.ServiceProviderService;
import com.berke.subscriptionmanager.service.SubscriptionService;
import com.berke.subscriptionmanager.service.UserService;
import lombok.extern.java.Log;
import org.hibernate.annotations.NotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/subs")
@Log
public class SubcriptionController extends AuthHelper {
    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private UserService userService;

    @Autowired
    private ServiceProviderService serviceProviderService;

    @GetMapping("/")
    public String getAllSubscriptions(Model model) {

        log.info("Subscription Controller" + userService.getCurrentUser());
        List<Subscription> subsList = subscriptionService.getAllSubscriptions(userService.getCurrentUser());

        //LOG
        subsList.stream().forEach(subs -> log.info(subs.toString()));

        model.addAttribute("subscription", subsList);

        return "subscription/subscription-list";

    }

    /**
     * @param model
     * @return
     */
    @GetMapping("/newsubscription")
    public String showSubscriptionForm(Model model) {
        List<ServiceProvider> currentServices = serviceProviderService.getAllServiceProviders();

        model.addAttribute("servicesProviders", currentServices);

        model.addAttribute("subscriptiondto", new SubscriptionDto());

        return "subscription/new-subscription";
    }

    @PostMapping("/addnewsubscription")
    public String addNewSubscription(@ModelAttribute("subscriptiondto") @Valid SubscriptionDto subscription,
                                     BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "subscription/new-subscription";
        }
        if (subscription.getBeginDate() == null || subscription.getEndDate() == null) {
            showSubscriptionForm(model);
        }
        ServiceProvider serviceProvider = serviceProviderService.getServiceProviderById(subscription.getProviderId())
                .orElseThrow();

        subscriptionService.addSubscription(new Subscription(userService.getCurrentUser(), serviceProvider,
                subscription.getBeginDate(), subscription.getEndDate(), subscription.getUsernameToLogin(),
                subscription.getPasswordToLogin()));


        return "redirect:/subs/";
    }

    @GetMapping("/updatesubscription/{id}")
    public String updateSubscriptionForm(@PathVariable long id, Model model) {
        Subscription subscription = subscriptionService.getSubscriptionById(id)
                .orElseThrow(() -> new SubscriptionException("Subscription Not found that given id"));

        List<ServiceProvider> serviceProvidersList = serviceProviderService.getAllServiceProviders();

        SubscriptionDto subscriptionDto = toSubscriptionDto(subscription);
        model.addAttribute("subscriptiondto", subscriptionDto);
        model.addAttribute("serviceproviders", serviceProvidersList);

        return "subscription/update-subscription";
    }


    @PostMapping("/updatesubscription")
    public String updateSubscription(@ModelAttribute("subscriptiondto") @Valid SubscriptionDto subscriptionDto,
                                     BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "subscription/update-subscription";
        }

        if (subscriptionDto.getBeginDate() == null || subscriptionDto.getEndDate() == null) {
            updateSubscriptionForm(subscriptionDto.getId(), model);
        }

        Subscription subscription = toSubscription(subscriptionDto);

        log.info("UPDATED SUBSCRIPTION " + subscription.toString());

        subscriptionService.updateSubscription(subscription);

        return "redirect:/subs/";
    }


    @GetMapping("/deletesubscription/{id}")
    public String deleteSubscription(@PathVariable int id) {
        subscriptionService.deleteSubscriptionById(id);
        return "redirect:/subs/";
    }


    private SubscriptionDto toSubscriptionDto(Subscription subscription) {
        SubscriptionDto subscriptionDto = new SubscriptionDto();
        subscriptionDto.setId(subscription.getId());
        subscriptionDto.setProviderId(subscription.getServiceProvider().getId());
        subscriptionDto.setProviderName(subscription.getServiceProvider().getProvider().getProviderName());
        subscriptionDto.setMembershipPlan(subscription.getServiceProvider().getMembershipPlan());
        subscriptionDto.setMontlyFee(subscription.getServiceProvider().getMonthlyFee());
        subscriptionDto.setBeginDate(subscription.getSubsBeginDate());
        subscriptionDto.setEndDate(subscription.getSubsEndDate());
        subscriptionDto.setPasswordToLogin(subscription.getPassWordToLogin());
        subscriptionDto.setUsernameToLogin(subscription.getUserNameToLogin());
        return subscriptionDto;
    }


    private Subscription toSubscription(SubscriptionDto subscriptionDto) {
        Subscription subscription = new Subscription();
        subscription.setId(subscriptionDto.getId());
        subscription.setSubscriptionUser(userService.getCurrentUser());
        subscription.setSubsBeginDate(subscriptionDto.getBeginDate());
        subscription.setSubsEndDate(subscriptionDto.getEndDate());
        subscription.setServiceProvider(serviceProviderService.getServiceProviderById(subscriptionDto.getProviderId())
                .orElseThrow(() -> new ServiceProviderException("Invalid service provider id " + subscriptionDto.getProviderId())));
        subscription.setPassWordToLogin(subscriptionDto.getPasswordToLogin());
        subscription.setUserNameToLogin(subscriptionDto.getUsernameToLogin());
        return subscription;
    }
}
