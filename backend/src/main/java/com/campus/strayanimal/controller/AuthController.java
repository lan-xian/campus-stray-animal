// backend/src/main/java/com/campus/strayanimal/controller/AuthController.java
package com.campus.strayanimal.controller;

import com.campus.strayanimal.common.Result;
import com.campus.strayanimal.dto.LoginRequest;
import com.campus.strayanimal.dto.LoginResponse;
import com.campus.strayanimal.entity.User;
import com.campus.strayanimal.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody LoginRequest request) {
        User user = authService.login(request.getUsername(), request.getPassword());
        return Result.success(new LoginResponse(user.getId(), user.getUsername(), user.getRole()));
    }

    @PostMapping("/register")
    public Result<LoginResponse> register(@RequestBody LoginRequest request) {
        User user = authService.register(request.getUsername(), request.getPassword());
        return Result.success(new LoginResponse(user.getId(), user.getUsername(), user.getRole()));
    }
}