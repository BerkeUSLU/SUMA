package com.berke.subscriptionmanager.controller;

import com.berke.subscriptionmanager.entity.Provider;
import com.berke.subscriptionmanager.entity.ServiceProvider;
import com.berke.subscriptionmanager.entity.dto.ServiceProviderDto;
import com.berke.subscriptionmanager.exception.ServiceProviderException;
import com.berke.subscriptionmanager.service.ProviderService;
import com.berke.subscriptionmanager.service.ServiceProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.nio.file.ProviderNotFoundException;
import java.util.List;

@Controller
@RequestMapping("/serviceprovider")
public class ServiceProviderController {
    @Autowired
    private ServiceProviderService serviceProviderService;

    @Autowired
    private ProviderService providerService;

    @GetMapping("/listserviceprovider")
    public String listAllProviders(Model model) {
        List<ServiceProvider> allServiceProviders = serviceProviderService.getAllServiceProviders();
        model.addAttribute("serviceproviders", allServiceProviders);

        return "serviceprovider/serviceprovider-list";
    }

    @GetMapping("/newprovider")
    public String displayNewProviderFrom(Model model) {
        List<Provider> providerList = providerService.getAllProviders();

        model.addAttribute("providers", providerList);
        model.addAttribute("serviceprovider", new ServiceProviderDto());
        return "serviceprovider/new-serviceprovider";
    }

    @PostMapping("/addnewprovider")
    public String addNewServiceProvider(@ModelAttribute("serviceprovider") @Valid ServiceProviderDto serviceProvider,
                                        BindingResult result) {
        if(result.hasErrors()) {
            return "serviceprovider/new-serviceprovider";
        }

        Provider provider = providerService.getProviderById(serviceProvider.getProviderId())
                .orElseThrow(() -> new ServiceProviderException("Service provider not foun to add new Service Provider"));

        ServiceProvider newServiceProvider = new ServiceProvider();
        newServiceProvider.setProvider(provider);
        newServiceProvider.setMembershipPlan(serviceProvider.getMembershipPlan());
        newServiceProvider.setMonthlyFee(serviceProvider.getMonthlyFee());

        serviceProviderService.updateServiceProvider(newServiceProvider);

        return "redirect:/serviceprovider/listserviceprovider";

    }

    @GetMapping("/updateprovider/{id}")
    public String updateProviderForm(@PathVariable int id, Model model) {
        ServiceProvider serviceProvider = serviceProviderService.getServiceProviderById(id)
                .orElseThrow(() -> new ServiceProviderException("Provider not found thet given id " + id));

        List<Provider> providerList = providerService.getAllProviders();

        ServiceProviderDto providerDto = new ServiceProviderDto(id, serviceProvider.getProvider().getId(),
                serviceProvider.getMembershipPlan(), serviceProvider.getMonthlyFee());

        model.addAttribute("providers", providerList);
        model.addAttribute("serviceprovider", providerDto);

        return "serviceprovider/update-serviceprovider";
    }

    @PostMapping("/updateprovider")
    public String updateProvider(@Valid @ModelAttribute("provider") ServiceProviderDto providerDto, BindingResult result) {
        if (result.hasErrors()) {
            return "serviceprovider/update-serviceprovider";
        }

        Provider provider = providerService.getProviderById(providerDto.getProviderId())
                .orElseThrow(() -> new ServiceProviderException("Provider not found given id " + providerDto.getProviderId()));


        serviceProviderService.updateServiceProvider(new ServiceProvider(providerDto.getId(),
                provider, providerDto.getMembershipPlan(), providerDto.getMonthlyFee()));

        return "redirect:/serviceprovider/listserviceprovider";
    }

    @GetMapping("/deleteprovider/{id}")
    public String deleteProvider(@PathVariable int id) {
        serviceProviderService.deleteServiceProviderById(id);
        return "redirect:/serviceprovider/listserviceprovider";
    }


}
