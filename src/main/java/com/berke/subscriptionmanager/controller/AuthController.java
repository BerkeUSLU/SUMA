package com.berke.subscriptionmanager.controller;

import com.berke.subscriptionmanager.controller.event.OnRegistrationCompleteEvent;
import com.berke.subscriptionmanager.entity.VerificationToken;
import com.berke.subscriptionmanager.entity.dto.PasswordDto;
import com.berke.subscriptionmanager.entity.user.User;
import com.berke.subscriptionmanager.entity.user.UserRole;
import com.berke.subscriptionmanager.exception.UserException;
import com.berke.subscriptionmanager.repository.VerificationTokenRepository;
import com.berke.subscriptionmanager.service.CustomUserDetailsService;
import com.berke.subscriptionmanager.service.EmailServiceImpl;
import com.berke.subscriptionmanager.service.SubscriptionService;
import com.berke.subscriptionmanager.service.UserService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/auth")
@Log
public class AuthController {
    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Autowired
    EmailServiceImpl emailService;

    /**
     * Return currently logged in  user name
     *
     * @param principal
     * @return
     */
    @GetMapping("/username")
    @ResponseBody
    public String currentUserName(Principal principal) {
        return principal.getName();
    }


    @GetMapping("/signup")
    public String showSignUpForm(User user) {
        return "user/add-user";
    }

    @PostMapping("/adduser")
    public String addUser(@Valid User user, BindingResult result, Model model, HttpServletRequest request) {
        if (result.hasErrors()) {
            log.severe("Add user has errors");
            return "user/add-user";
        }

        if(user.getEmail().isEmpty() || user.getFirstName().isEmpty() || user.getLastName().isEmpty()
                || user.getPassword().isEmpty()) {
            return showSignUpForm(user);
        }

        userService.saveNewUser(user);

        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, request.getContextPath()));


        log.info("New User persist");
        return "redirect:/index";
    }

    @GetMapping("/forgotpassword")
    public String forgotPasswordForm(Model model){
        return "user/forgot-password-email";
    }

    @PostMapping("/resetpassword")
    public String resetPassword(HttpServletRequest request,
                                         @RequestParam("email") String userEmail) {
        User user = userService.getUserByUsername(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User name not found"));

        String token = UUID.randomUUID().toString();
        userService.createVerificationToken(user, token);

        String confirmationUrl ="/auth/changepassword?token=" + token;

        emailService.sendMessage(user.getEmail(), "Reset Password ",
                "Follow link to confirm your registration " + "\r\n" + "http://localhost:9001" + confirmationUrl);

        return "redirect:/login";

    }
    @GetMapping("/changepassword")
    public String showChangePasswordPage(Model model,
                                         @RequestParam("token") String token) {
        Optional<VerificationToken> verificationToken = userService.getVerificationToken(token);
        verificationToken.orElseThrow(() -> new UsernameNotFoundException("Invalid Verification Token"));

        Optional<User> user = userService.getUser(token);
        //String result = securityService.validatePasswordResetToken(token);
        if(user.isEmpty()) {

            return "redirect:/auth/forgotpassword";
        } else {

            model.addAttribute("passworddto", new PasswordDto(token, ""));
            return "user/change-password";
        }
    }

    @PostMapping("/savepassword")
    public String savePassword(@Valid PasswordDto passwordDto) {

        Optional<VerificationToken> verificationToken = userService.getVerificationToken(passwordDto.getToken());
        verificationToken.orElseThrow(() -> new UsernameNotFoundException("Invalid Verification Token"));

        Optional<User> user = userService.getUser(passwordDto.getToken());
        if(user.isPresent()) {
            user.get().setPassword(passwordDto.getPassword());
            userService.updateUser(user.get());

        }
        //TODO: error case
        return "redirect:/login";
    }




    @GetMapping("/confirmRegistration")
    public String confirmRegistration(WebRequest request, Model model, @RequestParam("token") String token) {


        Optional<VerificationToken> verificationToken = userService.getVerificationToken(token);
        verificationToken.orElseThrow(() -> new UsernameNotFoundException("Invalid Verification Token"));

        Optional<User> user = userService.getUser(token);
        user.orElseThrow(() -> new UsernameNotFoundException("Invalid Verification Token"));;




       user.get().setEnabled(true);
        //service.updateUser(user);  rehashes the password

        userService.activateUser(user.get());

        return "redirect:/login";
    }

    @GetMapping("/updateuser")
    public String updateCurrentUserForm(Model model) {
        User user = userService.getCurrentUser();
        log.info("Current user will be updated " + user.toString());

        model.addAttribute("user", user);
        return "user/update-user";
    }

    @PostMapping("/updateuser")
    public String updateUser(@Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "user/update-user";
        }
        log.warning("Password " + user.getPassword());

        if(user.getPassword() == null || user.getPassword().trim().length() == 0){
            log.warning("UPDATE pass");
            return showUpdateForm(user.getId(), model);
        }

        log.info("Updated user info " + user.toString());
        user.setEnabled(true);
        userService.updateUser(user);
        return "redirect:/index";
    }

    @GetMapping("/updateuser/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        User user = userService.getUserById(id).orElseThrow(() -> new UsernameNotFoundException("Can not found user"));

        model.addAttribute("user", user);
        return "user/update-user";
    }

    @GetMapping("/deleteuser")
    public String deleteUser(Model model) {
        User user = userService.getCurrentUser();

        long size = subscriptionService.getAllSubscriptions(user).stream().count();
        if (size > 0) {
            model.addAttribute("infoMessage", "Please delete all your subscriptions first");
            return "info";
        }

        userService.deleteUser(user);
        userService.logoutCurrentUser();
        return "redirect:/index";
    }

    @GetMapping("/deleteuser/{id}")
    public String deleteUserById(@PathVariable("id") Long id) {
        User user = userService.getUserById(id).orElseThrow(() -> new UsernameNotFoundException("Can not found user"));

        subscriptionService.deleteAllSubscriptionsByUser(user);

        userService.deleteUser(user);

        return "redirect:/auth/listusers";
    }


    @GetMapping("/listusers")
    public String listUsers(Model model) {
        List<User> userList = userService.getAllUsers();

        log.info("User list" + userList.stream().map(User::toString).collect(Collectors.joining("\n")));

        model.addAttribute("userlist", userList);

        return "user/list-users";
    }
}
