package com.projects.ExpenseTracker.service;

import com.projects.ExpenseTracker.dto.UserLoginRequest;
import com.projects.ExpenseTracker.dto.UserRegisterRequest;

public interface UserService {
    void register(UserRegisterRequest request);
    String login(UserLoginRequest request);

}
