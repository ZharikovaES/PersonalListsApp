package com.ZharikovaES.PersonalListsApp.services;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ZharikovaES.PersonalListsApp.models.Role;
import com.ZharikovaES.PersonalListsApp.models.User;

import jakarta.security.auth.message.AuthException;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Service
public class AuthService {
    private final UserService userService;
    private final Map<String, String> refreshStorage = new HashMap<>();
    private final JwtProvider jwtProvider;
    private final MailSender mailSender;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(UserService userService, JwtProvider jwtProvider, MailSender mailSender) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
        this.mailSender = mailSender;
    }

    public String registration(RegistrationRequest authRequest) throws AuthException {
        if (authRequest == null || authRequest.getUsername() == null) {
            throw new AuthException("Некорректный запрос на регистрацию");
        }

        if (userService.existsUserByUsername(authRequest.getUsername())) {
            throw new AuthException("Пользователь уже существует!");
        }

        User newUser = new User();
        newUser.setActive(true);
        newUser.setRoles(Collections.singleton(Role.USER));
        newUser.setActivationCode(UUID.randomUUID().toString());
        newUser.setPassword(passwordEncoder.encode(authRequest.getPassword()));
        newUser.setUsername(authRequest.getUsername());

        Date createdDate = new Date();
        newUser.setDateRegistration(createdDate);
        newUser.setDateLastActivity(createdDate);

        if (authRequest.getEmail() == null || authRequest.getEmail().isEmpty()) {
            throw new AuthException("Email отсутствует!");
        }

        newUser.setEmail(authRequest.getEmail());

        String messageMail = String.format(
            "Здравствуй, %s! \nДобро пожаловать на сервис \"Personal Lists\"." +
            "\nПерейдите по ссылке для подтверждения почты аккаунта: http://localhost:8080/activate/%s",
            newUser.getUsername(),
            newUser.getActivationCode()
        );

        mailSender.send(newUser.getEmail(), "Activation code", messageMail);
        userService.addNewUser(newUser);

        return "Письмо для подтверждения аккаунта отправлено на электронный адрес: " + newUser.getEmail();
    }

    public JwtResponse login(JwtRequest authRequest) throws AuthException {
        if (authRequest == null) {
            throw new AuthException("Некорректные данные");
        }

        User user;
        try {
            user = userService.loadUserByUsername(authRequest.getLogin());
        } catch (UsernameNotFoundException e) {
            throw new AuthException("Пользователь не найден");
        }

        if (passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            String accessToken = jwtProvider.generateAccessToken(user);
            String refreshToken = jwtProvider.generateRefreshToken(user);
            refreshStorage.put(user.getUsername(), refreshToken);
            return new JwtResponse(accessToken, refreshToken);
        } else {
            throw new AuthException("Неправильный пароль");
        }
    }

    public JwtResponse getAccessToken(String refreshToken) throws AuthException {
        if (refreshToken == null) {
            throw new AuthException("Токен отсутствует");
        }

        if (jwtProvider.validateRefreshToken(refreshToken)) {
            String login = jwtProvider.getUsernameFromRefreshToken(refreshToken);
            String savedRefreshToken = refreshStorage.get(login);

            if (savedRefreshToken != null && savedRefreshToken.equals(refreshToken)) {
                User user = userService.loadUserByUsername(login);
                String newAccessToken = jwtProvider.generateAccessToken(user);
                return new JwtResponse(newAccessToken, null);
            }
        }

        return new JwtResponse(null, null);
    }

    public JwtResponse refresh(String refreshToken) throws AuthException {
        if (refreshToken == null) {
            throw new AuthException("Токен отсутствует");
        }

        if (jwtProvider.validateRefreshToken(refreshToken)) {
            String login = jwtProvider.getUsernameFromRefreshToken(refreshToken);
            String savedRefreshToken = refreshStorage.get(login);

            if (savedRefreshToken != null && savedRefreshToken.equals(refreshToken)) {
                User user = userService.loadUserByUsername(login);
                String newAccessToken = jwtProvider.generateAccessToken(user);
                String newRefreshToken = jwtProvider.generateRefreshToken(user);
                refreshStorage.put(user.getUsername(), newRefreshToken);
                return new JwtResponse(newAccessToken, newRefreshToken);
            }
        }

        throw new AuthException("Невалидный JWT токен");
    }

    public JwtAuthentication getAuthInfo() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }
}
