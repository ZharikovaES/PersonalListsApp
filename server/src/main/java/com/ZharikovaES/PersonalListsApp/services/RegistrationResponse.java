package com.ZharikovaES.PersonalListsApp.services;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegistrationResponse {
  private final String type = "Bearer";
  private String accessToken;
  private String refreshToken;
}
