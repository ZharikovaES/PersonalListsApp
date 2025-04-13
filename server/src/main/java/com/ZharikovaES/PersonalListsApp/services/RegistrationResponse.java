package com.ZharikovaES.PersonalListsApp.services;

public class RegistrationResponse {
  private String message;

  public RegistrationResponse(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
      this.message = message;
  }
}
