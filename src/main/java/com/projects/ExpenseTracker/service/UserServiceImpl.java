package com.projects.ExpenseTracker.service;

import com.projects.ExpenseTracker.dto.UserLoginRequest;
import com.projects.ExpenseTracker.dto.UserRegisterRequest;
import com.projects.ExpenseTracker.exception.EmailAlreadyExistsException;
import com.projects.ExpenseTracker.exception.InvalidCredentialsException;
import com.projects.ExpenseTracker.model.User;
import com.projects.ExpenseTracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void register(UserRegisterRequest request) {

        userRepository.findByEmail(request.getEmail())
                .ifPresent(user -> {throw new EmailAlreadyExistsException("Email already Registered!");});

        User user= new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

    }

    public void login(UserLoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new InvalidCredentialsException("Invalid email or password"));
        boolean passwordMatch= passwordEncoder
                .matches(request.getPassword(),user.getPassword());

        if(!passwordMatch){
            throw new InvalidCredentialsException("Invalid email or password");
        }
    }


}
