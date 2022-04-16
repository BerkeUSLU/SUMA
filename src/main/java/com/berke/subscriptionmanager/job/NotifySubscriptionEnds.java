package com.berke.subscriptionmanager.job;

import com.berke.subscriptionmanager.service.EmailServiceImpl;
import com.berke.subscriptionmanager.entity.Subscription;
import com.berke.subscriptionmanager.service.SubscriptionService;
import com.berke.subscriptionmanager.service.UserService;
import lombok.extern.java.Log;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Log
public class NotifySubscriptionEnds implements Job {
    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private UserService userService;

    @Autowired
    EmailServiceImpl emailService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("JOB EXECUTION STARTED");
        notifyUserToRenew(30, "Hello ");
    }

    private void notifyUserToRenew(int day, String body) {
        List<Subscription> allSubscriptions = subscriptionService.getAllSubscriptionsThatEndsInDays(day);

        allSubscriptions.stream().forEach(
                subs -> emailService.sendMessage(subs.getSubscriptionUser().getEmail(),
                        "Last " + day + " day(s) to renew/cancel to yout subscription", body)
        );

        log.info(allSubscriptions.size() + " user(s) notified for last " + day +  " days");
    }
}
