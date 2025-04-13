package com.ZharikovaES.PersonalListsApp.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ZharikovaES.PersonalListsApp.services.AuthService;
import com.ZharikovaES.PersonalListsApp.services.JwtAuthentication;

@RestController
@RequestMapping("api")
public class Controller {
    private final AuthService authService;

    public Controller(AuthService authService) {
      this.authService = authService;
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("user")
    public ResponseEntity<String> helloUser() {
        final JwtAuthentication authInfo = authService.getAuthInfo();
        return ResponseEntity.ok("Hello user " + authInfo.getPrincipal() + "!");
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("admin")
    public ResponseEntity<String> helloAdmin() {
        final JwtAuthentication authInfo = authService.getAuthInfo();
        return ResponseEntity.ok("Hello admin " + authInfo.getPrincipal() + "!");
    }

}