package com.ZharikovaES.PersonalListsApp.services;

import io.jsonwebtoken.Claims;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ZharikovaES.PersonalListsApp.models.User;

import java.util.HashMap;
import java.util.Map;

import javax.security.auth.message.AuthException;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final Map<String, String> refreshStorage = new HashMap<>();
    private final JwtProvider jwtProvider;

    public RegistrationResponse registration(@NonNull JwtRequest authRequest) throws AuthException {
      User userFromDb = userRepo.findByUsername(user.getUsername());

        if (userFromDb != null) {
            model.put("message", "Пользователь уже существует!");
            return "registration";
        }
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Date createdDate = new Date();
        user.setDateRegistration(createdDate);
        user.setDateLastActivity(createdDate);
        userRepo.save(user);

        if (!StringUtils.isEmpty(user.getEmail())){

            String message = String.format(
                "Здравствуй, %s! \n" + "Добро пожаловать на сервис \"Personal Lists\".\nПерейдите по ссылке для подтверждения почты аккаунта: http://localhost:8080/activate/%s",
                    user.getUsername(),
                    user.getActivationCode()
            );
            mailSender.send(user.getEmail(), "Activation code", message);
            model.put("message", "Письмо для подтверждения аккаунта отправлено на электронный адрес: " + user.getEmail());
        }
        return "send-email-message";
    }

    public JwtResponse login(@NonNull JwtRequest authRequest) throws AuthException {
        User user = null;
        try {
          user = userService.loadUserByUsername(authRequest.getLogin());
        } catch (UsernameNotFoundException err) {
          throw new AuthException("Пользователь не найден");
        }

        if (user.getPassword().equals(authRequest.getPassword())) {
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