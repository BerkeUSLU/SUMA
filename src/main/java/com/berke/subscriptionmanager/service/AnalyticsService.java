package com.berke.subscriptionmanager.service;

import com.berke.subscriptionmanager.entity.dto.SubsByProviderDto;
import com.berke.subscriptionmanager.repository.SubscriptionRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log
public class AnalyticsService {
    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private UserService userService;

    public List<SubsByProviderDto> getCostByProvider() {

        log.info("getCostByProvider ANALYTICS SERVICE");

        List<SubsByProviderDto> result = subscriptionRepository
                .getSubscriptionsMonthlyByServiceProvider(userService.getCurrentUser().getId());

        log.info("SUBS-By_PROVIDER" + result.stream().map(SubsByProviderDto::toString).collect(Collectors.joining("\n")));
        return result;

    }

    public Pair<Double, Double> getMonthlyAndYearlyExpenses() {
        Optional<Double> monthly = subscriptionRepository
                .getMonthlyExpenses(userService.getCurrentUser().getId());

        log.info("Monthly Expenses" + monthly);

        Double expense = monthly.orElse(0.);


        return Pair.of(expense, expense * 12);

    }

}
