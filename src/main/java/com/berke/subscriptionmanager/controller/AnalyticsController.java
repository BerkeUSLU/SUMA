package com.berke.subscriptionmanager.controller;

import com.berke.subscriptionmanager.entity.dto.SubsByProviderDto;
import com.berke.subscriptionmanager.service.AnalyticsService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/analytics")
@Log
public class AnalyticsController {
    @Autowired
    private AnalyticsService analyticsService;

    @GetMapping({"", "/"})
    public String getSubscriptionAnalysis(Model model) {
        List<SubsByProviderDto> subsByProviderDtoList = analyticsService.getCostByProvider();
        Map<String, Double> subsByProviderMap = subsByProviderDtoList.stream()
                .collect(Collectors
                        .toMap(SubsByProviderDto::getProviderName, SubsByProviderDto::getSumFee));

        Pair<Double, Double> monthlyAndYearlyExpenses = analyticsService.getMonthlyAndYearlyExpenses();

        log.info("MAP of EXPENSES " + Map.of("Monthly", monthlyAndYearlyExpenses.getFirst(),
                "Yearly", monthlyAndYearlyExpenses.getSecond()).values().stream().map(Object::toString).
                collect(Collectors.joining("\n ")));

        model.addAttribute("subsbyprovider", subsByProviderMap);
        model.addAttribute("expenses", Map.of("Monthly", monthlyAndYearlyExpenses.getFirst(),
                "Yearly", monthlyAndYearlyExpenses.getSecond()));


        return "analytics/base-analytics";
    }



}