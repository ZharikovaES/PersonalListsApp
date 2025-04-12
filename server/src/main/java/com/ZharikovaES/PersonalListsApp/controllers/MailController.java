package com.ZharikovaES.PersonalListsApp.controllers;

import com.ZharikovaES.PersonalListsApp.models.User;
import com.ZharikovaES.PersonalListsApp.services.AuthService;
import com.ZharikovaES.PersonalListsApp.services.MailResponse;
import com.ZharikovaES.PersonalListsApp.services.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class MailController {
    private final UserService userService;
    private final AuthService authService;

    @GetMapping("/activate/{code}")
    public ResponseEntity<MailResponse> activate(Model model, @PathVariable String code) {
        MailResponse mailResponse = null;
        User user = userService.activateUser(code);
        String message = null;
        
        if (user != null) {
            message = "Пользователь успешно подтвердил свой аккаунт";
        } else  {
            message = "Код активации не найден";
        }

        mailResponse = new MailResponse(message);
        return ResponseEntity.ok(mailResponse);
    }
}
