package com.ZharikovaES.PersonalListsApp.controllers;

import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ZharikovaES.PersonalListsApp.services.AuthService;
import com.ZharikovaES.PersonalListsApp.services.JwtRequest;
import com.ZharikovaES.PersonalListsApp.services.JwtResponse;
import com.ZharikovaES.PersonalListsApp.services.RegistrationResponse;

import jakarta.security.auth.message.AuthException;

import com.ZharikovaES.PersonalListsApp.services.RefreshJwtRequest;
import com.ZharikovaES.PersonalListsApp.services.RegistrationRequest;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
      this.authService = authService;
    }

    @PostMapping("registration")
    public ResponseEntity<RegistrationResponse> registration(@RequestBody RegistrationRequest authRequest) {
        RegistrationResponse authData = null;
        try {
          String message = authService.registration(authRequest);
          authData = new RegistrationResponse(message);
        } catch (AuthException e) {
          return ResponseEntity
                  .status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(authData);
    }
    @PostMapping("login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest authRequest) {
        JwtResponse token = null;
        try {
          token = authService.login(authRequest);
        } catch (AuthException e) {
          return ResponseEntity
                  .status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(token);
    }

    @PostMapping("token")
    public ResponseEntity<JwtResponse> getNewAccessToken(@RequestBody RefreshJwtRequest request) {
        JwtResponse token;
        try {
          token = authService.getAccessToken(request.getRefreshToken());
        } catch (AuthException e) {
          return ResponseEntity
                  .status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(token);
    }

    @PostMapping("refresh")
    public ResponseEntity<JwtResponse> getNewRefreshToken(@RequestBody RefreshJwtRequest request) {
        JwtResponse token;
        try {
          token = authService.refresh(request.getRefreshToken());
        } catch (AuthException e) {
          return ResponseEntity
                  .status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(token);
    }
}