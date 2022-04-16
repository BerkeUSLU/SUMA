package com.berke.subscriptionmanager.service;

import com.berke.subscriptionmanager.entity.VerificationToken;
import com.berke.subscriptionmanager.entity.user.CustomUserDetails;
import com.berke.subscriptionmanager.entity.user.User;
import com.berke.subscriptionmanager.repository.UserRepository;
import com.berke.subscriptionmanager.repository.VerificationTokenRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Log
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    public Optional<User> saveNewUser(User user) {
        try {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUserName(user.getEmail());
        userRepository.save(user);
        return Optional.of(user);
    }
        catch (Exception ex) {
        return Optional.empty();
    }
    }
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUserName(username);
    }

    public void deleteUser(User user) {
        verificationTokenRepository.deleteByUser(user);
        userRepository.delete(user);
    }

    public Optional<User> updateUser(User user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setUserName(user.getEmail());
            //return userRepository.findById(user.getId());
            return Optional.of(userRepository.save(user));
        }
        catch (Exception ex) {
            return Optional.empty();
        }
    }

    /*public List<User> getAllUserById(List<Integer> userIds) {
        return userRepository.getAllUserById(userIds);
    }*/



    public User getCurrentUser() {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CustomUserDetails customUserDetails = (CustomUserDetails)customUserDetailsService.loadUserByUsername(principal.getUsername());
        log.info("CURRENT USER " + customUserDetails.getUser().toString());
        return customUserDetails.getUser();
    }

    public void logoutCurrentUser() {
        SecurityContextHolder.clearContext();

    }

    public Optional<User> getUser(String verificationToken) {
        return Optional.of(verificationTokenRepository.findByToken(verificationToken).getUser());

    }
    public Optional<VerificationToken> getVerificationToken(String VerificationToken) {
        return Optional.of(verificationTokenRepository.findByToken(VerificationToken));
    }

    public void createVerificationToken(User user, String token) {
        VerificationToken newToken = new VerificationToken(token, user);
        verificationTokenRepository.save(newToken);
    }


    public void activateUser(User user) {
        userRepository.activateUser(user.getId());
    }
}
