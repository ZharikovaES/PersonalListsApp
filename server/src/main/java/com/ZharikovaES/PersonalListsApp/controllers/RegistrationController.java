package com.ZharikovaES.PersonalListsApp.controllers;

import com.ZharikovaES.PersonalListsApp.models.Role;
import com.ZharikovaES.PersonalListsApp.models.User;
import com.ZharikovaES.PersonalListsApp.repos.UserRepo;
import com.ZharikovaES.PersonalListsApp.services.MailSender;
import com.ZharikovaES.PersonalListsApp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.*;

@Controller
public class RegistrationController {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private MailSender mailSender;

    // @Autowired
    // private PasswordEncoder passwordEncoder;

    // @Bean
    // PasswordEncoder passwordEncoder(){
    //     PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    //     return passwordEncoder;
    // }

    // @GetMapping("/registration")
    // public String registration(){

    //     return "registration";
    // }

    // @PostMapping("/api/registration")
    // public String addUser(User user, Map<String, Object> model){
    //     User userFromDb = userRepo.findByUsername(user.getUsername());

    //     if (userFromDb != null) {
    //         model.put("message", "Пользователь уже существует!");
    //         return "registration";
    //     }
    //     user.setActive(true);
    //     user.setRoles(Collections.singleton(Role.USER));
    //     user.setActivationCode(UUID.randomUUID().toString());
    //     user.setPassword(passwordEncoder.encode(user.getPassword()));
    //     Date createdDate = new Date();
    //     user.setDateRegistration(createdDate);
    //     user.setDateLastActivity(createdDate);
    //     userRepo.save(user);

    //     if (!StringUtils.isEmpty(user.getEmail())){

    //         String message = String.format(
    //             "Здравствуй, %s! \n" + "Добро пожаловать на сервис \"Personal Lists\".\nПерейдите по ссылке для подтверждения почты аккаунта: http://localhost:8080/activate/%s",
    //                 user.getUsername(),
    //                 user.getActivationCode()
    //         );
    //         mailSender.send(user.getEmail(), "Activation code", message);
    //         model.put("message", "Письмо для подтверждения аккаунта отправлено на электронный адрес: " + user.getEmail());
    //     }
    //     return "send-email-message";
    // }

    @GetMapping("/api/activate/{code}")
    private String activate(Model model, @PathVariable String code) {
        boolean isActivated = userService.activateUser(code);

        if (isActivated) {
            model.addAttribute("message", "Пользователь успешно подтвердил свой аккаунт");
        } else  {
            model.addAttribute("message", "Код активации не найден");
        }
        return "login";
    }
}
