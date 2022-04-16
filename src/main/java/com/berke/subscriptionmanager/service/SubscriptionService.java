package com.berke.subscriptionmanager.service;

import com.berke.subscriptionmanager.entity.Subscription;
import com.berke.subscriptionmanager.entity.user.User;
import com.berke.subscriptionmanager.repository.SubscriptionRepository;
import com.berke.subscriptionmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionService {
    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private UserRepository userRepository;


    public Optional<Subscription> getSubscriptionById(long id) {
        return subscriptionRepository.findById(id);
    }

    public List<Subscription> getAllSubscriptions(User user) {
        return subscriptionRepository.getSubscriptionsByUserId(user.getId());
    }


    public Optional<Subscription> updateSubscription(Subscription subscription) {
        try {
            if(subscription.getPassWordToLogin().isEmpty()) {
                subscription.setPassWordToLogin("-");
            }
            if(subscription.getUserNameToLogin().isEmpty()){
                subscription.setUserNameToLogin("-");
            }
            subscriptionRepository.save(subscription);
            return subscriptionRepository.findById(subscription.getId());
        }
        catch (Exception ex) {
            return Optional.empty();
        }
    }


    public Optional<Subscription> addSubscription(Subscription subscription) {
       return updateSubscription(subscription);
    }

    public void deleteSubscription(Subscription subscription) {
        subscriptionRepository.delete(subscription);
    }

    public void deleteAllSubscriptionsByUser(User user) {
        subscriptionRepository.deleteSubscriptionsByUser(user.getId());
    }

    public void deleteSubscriptionById(long id) {
        subscriptionRepository.deleteById(id);
    }

    public List<Subscription> getAllSubscriptionsThatEndsInDays(int days) {

        return subscriptionRepository.findAllBySubsEndDate(LocalDate.now().minusDays(days));
    }

    public void deactivateExpiredSubscriptions() {
        subscriptionRepository.deactivateExpiredSubscriptions();
    }

}
