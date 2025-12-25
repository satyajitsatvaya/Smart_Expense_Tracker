package com.projects.ExpenseTracker.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter  @Setter
public class UserLoginRequest {

    @Email(message = "Invalid Email Format")
    @NotBlank(message = "Email is Required")
    private String email;

    @NotBlank(message = "Password is Required")
    private String password;
}
