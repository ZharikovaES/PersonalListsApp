package com.ZharikovaES.PersonalListsApp.controllers;

import com.ZharikovaES.PersonalListsApp.models.User;
import com.ZharikovaES.PersonalListsApp.services.ActivateCodeRequest;
import com.ZharikovaES.PersonalListsApp.services.AuthService;
import com.ZharikovaES.PersonalListsApp.services.MailResponse;
import com.ZharikovaES.PersonalListsApp.services.UserService;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("api/auth")
public class MailController {
    private final UserService userService;
    private final AuthService authService;

    public MailController(UserService userService, AuthService authService) {
      this.userService = userService;
      this.authService = authService;
    }

    @PostMapping("/activate")
    public ResponseEntity<MailResponse> activate(@RequestBody ActivateCodeRequest request) {
        MailResponse mailResponse = null;
        User user = userService.activateUser(request.getCode());
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
