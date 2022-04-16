package com.berke.subscriptionmanager.job;

import com.berke.subscriptionmanager.entity.Subscription;
import com.berke.subscriptionmanager.service.EmailServiceImpl;
import com.berke.subscriptionmanager.service.SubscriptionService;
import lombok.extern.java.Log;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;


@Log
public class DeActivateSubscriptionsThatExpires implements Job {
    @Autowired
    SubscriptionService subscriptionService;

    @Autowired
    EmailServiceImpl emailService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        subscriptionService.deactivateExpiredSubscriptions();
        log.info("Deactivate job has run");

    }
}
