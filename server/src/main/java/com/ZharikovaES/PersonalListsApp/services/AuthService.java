package com.ZharikovaES.PersonalListsApp.services;

import io.jsonwebtoken.Claims;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ZharikovaES.PersonalListsApp.models.Role;
import com.ZharikovaES.PersonalListsApp.models.User;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.security.auth.message.AuthException;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final Map<String, String> refreshStorage = new HashMap<>();
    private final JwtProvider jwtProvider;
    private final MailSender mailSender;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String registration(@NonNull RegistrationRequest authRequest) throws AuthException {
        if (userService.existsUserByUsername(authRequest.getUsername())) {
            throw new AuthException("Пользователь уже существует!");
        }
        User newUser = new User();
        newUser.setActive(true);
        newUser.setRoles(Collections.singleton(Role.USER));
        newUser.setActivationCode(UUID.randomUUID().toString());
        String password = authRequest.getPassword();
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setUsername(authRequest.getUsername());
        Date createdDate = new Date();
        newUser.setDateRegistration(createdDate);
        newUser.setDateLastActivity(createdDate);
        
        if (authRequest.getEmail().isEmpty()){
          throw new AuthException("Email отсутствует!");
        }
        newUser.setEmail(authRequest.getEmail());
        
        String messageMail = String.format("Здравствуй, %s! \n" + "Добро пожаловать на сервис \"Personal Lists\".\nПерейдите по ссылке для подтверждения почты аккаунта: http://localhost:8080/activate/%s",
          newUser.getUsername(),
          newUser.getActivationCode()
        );
        mailSender.send(newUser.getEmail(), "Activation code", messageMail);
        userService.addNewUser(newUser);

        return "Письмо для подтверждения аккаунта отправлено на электронный адрес: " + newUser.getEmail();
    }

    public JwtResponse login(@NonNull JwtRequest authRequest) throws AuthException {
        User user = null;
        try {
          user = userService.loadUserByUsername(authRequest.getLogin());
        } catch (UsernameNotFoundException err) {
          throw new AuthException("Пользователь не найден");
        }

        if (passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            final String accessToken = jwtProvider.generateAccessToken(user);
            final String refreshToken = jwtProvider.generateRefreshToken(user);
            refreshStorage.put(user.getUsername(), refreshToken);
            return new JwtResponse(accessToken, refreshToken);
        } else {
            throw new AuthException("Неправильный пароль");
        }
    }

    public JwtResponse getAccessToken(@NonNull String refreshToken) throws AuthException {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                User user = null;
                try{
                  user = userService.loadUserByUsername(login);
                } catch (UsernameNotFoundException err) {
                  throw new AuthException("Пользователь не найден");
                }
                final String accessToken = jwtProvider.generateAccessToken(user);
                return new JwtResponse(accessToken, null);
            }
        }
        return new JwtResponse(null, null);
    }

    public JwtResponse refresh(@NonNull String refreshToken) throws AuthException {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                User user = null;
                try{
                  user = userService.loadUserByUsername(login);
                } catch (UsernameNotFoundException err) {
                  throw new AuthException("Пользователь не найден");
                }
                final String accessToken = jwtProvider.generateAccessToken(user);
                final String newRefreshToken = jwtProvider.generateRefreshToken(user);
                refreshStorage.put(user.getUsername(), newRefreshToken);
                return new JwtResponse(accessToken, newRefreshToken);
            }
        }
        throw new AuthException("Невалидный JWT токен");
    }

    public JwtAuthentication getAuthInfo() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }

}