package com.ZharikovaES.PersonalListsApp.services;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationRequest {
  private String username;
  private String password;
  private String email;
}
