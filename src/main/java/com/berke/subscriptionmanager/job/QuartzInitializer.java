package com.berke.subscriptionmanager.job;

import lombok.extern.java.Log;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Log
@Component
public class QuartzInitializer {
    private final int SUBS_ENDS_INTERVAL_SEC = 1000;
    private final int SUBS_DEACTIVATE_INTERVAL_SEC = 1000;

    @Autowired
    private Scheduler scheduler;

    @PostConstruct
    private void init() {
        log.info("QuartzInitializer started");

        notifyUsersToEndsSubscription();
        deActivateSubscriptionsThatEnds();

    }

    private void deActivateSubscriptionsThatEnds() {
        JobDetail job = JobBuilder.newJob().ofType(DeActivateSubscriptionsThatExpires.class)
                .withIdentity("deactivateSubscriptions", "notifyGroup").build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("myTrigger2", "notifyGroup")
                .startNow()
                .withSchedule(SimpleScheduleBuilder
                        .simpleSchedule()
                        .withIntervalInSeconds(SUBS_DEACTIVATE_INTERVAL_SEC)
                        .repeatForever()
                )
                .build();
        try {
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            log.severe("Exception occured while scheduling notify user subscription ends" + e.getMessage());
        }
    }

    private void notifyUsersToEndsSubscription() {
        JobDetail job = JobBuilder.newJob().ofType(NotifySubscriptionEnds.class)
                .withIdentity("notifySubscriptionEnds", "notifyGroup").build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("myTrigger", "notifyGroup")
                .startNow()
                .withSchedule(SimpleScheduleBuilder
                        .simpleSchedule()
                        .withIntervalInSeconds(SUBS_ENDS_INTERVAL_SEC)
                        .repeatForever()
                )
                .build();
        try {
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            log.severe("Exception occured while scheduling notify user subscription ends" + e.getMessage());
        }
    }
}
