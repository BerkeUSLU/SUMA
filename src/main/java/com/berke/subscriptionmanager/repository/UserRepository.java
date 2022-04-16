package com.berke.subscriptionmanager.repository;

import com.berke.subscriptionmanager.entity.user.User;

import org.hibernate.sql.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String username);

    @Modifying
    @Transactional
    @Query(value = "UPDATE user SET enabled=1 WHERE user_id = :user_id ;", nativeQuery = true)
    void activateUser(@Param("user_id") long userId);

}