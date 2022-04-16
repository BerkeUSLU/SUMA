package com.berke.subscriptionmanager.repository;


import com.berke.subscriptionmanager.entity.VerificationToken;
import com.berke.subscriptionmanager.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import javax.transaction.Transactional;
import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);
    VerificationToken findByUser(User user);
    @Transactional
    @Modifying
    void deleteByUser(User user);
}
