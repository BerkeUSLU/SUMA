package com.berke.subscriptionmanager.service;

import com.berke.subscriptionmanager.entity.user.CustomUserDetails;
import com.berke.subscriptionmanager.entity.user.User;
import com.berke.subscriptionmanager.repository.UserRepository;

import lombok.Singular;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUserName(userName);
        log.info("USER DETAILS SERVICE " + user.toString() );

        user.orElseThrow(() -> new UsernameNotFoundException("Not found: " + userName));

        return user.map(CustomUserDetails::new).get();
    }



}
