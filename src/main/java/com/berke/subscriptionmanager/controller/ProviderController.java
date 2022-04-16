package com.berke.subscriptionmanager.controller;

import com.berke.subscriptionmanager.entity.Provider;
import com.berke.subscriptionmanager.entity.dto.ProviderDto;
import com.berke.subscriptionmanager.service.ProviderService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.nio.file.ProviderNotFoundException;
import java.util.List;

@Log
@Controller
@RequestMapping("/provider")
public class ProviderController {
    @Autowired
    ProviderService providerService;

    @GetMapping("/listproviders")
    public String listProviders(Model model) {
        List<Provider> allProviders = providerService.getAllProviders();

        model.addAttribute("providers", allProviders);

        return "provider/provider-list";
    }

    @GetMapping("/newprovider")
    public String newProviderForm(Model model) {
        model.addAttribute("providerdto", new ProviderDto());
        return "provider/new-provider";
    }

    @PostMapping("/addnewprovider")
    public String newProvider(@ModelAttribute("providerdto") @Valid ProviderDto provider, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "provider/new-provider";
        }
        log.info("NEW PROVIDER DTO " + provider + "------name" + provider.getProviderName());
        providerService.newProvider(new Provider(provider.getProviderName()));

        return "redirect:/provider/listproviders";
    }

    @GetMapping("/updateprovider/{id}")
    public String updateProviderForm(@PathVariable int id, Model model) {
        Provider provider = providerService.getProviderById(id)
                .orElseThrow(() -> new ProviderNotFoundException("Provider not found"));

        model.addAttribute("provider", provider);

        return "provider/update-provider";
    }

    @PostMapping("/updateprovider")
    public String updateProvider(@Valid @ModelAttribute("provider") Provider provider, BindingResult result) {
        if (result.hasErrors()) {
            return "provider/update-provider";
        }
        log.info("UPDATE PROVIDER " + provider.getId() + " --> " + provider.getProviderName());
        providerService.updateProvider(provider);
        return "redirect:/provider/listproviders";
    }

    @GetMapping("/deleteprovider/{id}")
    public String deleteProvider(@PathVariable int id) {
        providerService.deleteProviderById(id);

        return "redirect:/provider/listproviders";
    }

}
