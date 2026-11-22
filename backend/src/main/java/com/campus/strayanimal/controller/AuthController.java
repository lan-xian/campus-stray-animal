package com.campus.strayanimal.controller;

import com.campus.strayanimal.common.Result;
import com.campus.strayanimal.dto.LoginRequest;
import com.campus.strayanimal.dto.LoginResponse;
import com.campus.strayanimal.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse.LoginResult r = authService.login(request.getUsername(), request.getPassword());
        return Result.success(new LoginResponse(r.id(), r.username(), r.role(), r.token()));
    }

    @PostMapping("/register")
    public Result<LoginResponse> register(@Valid @RequestBody LoginRequest request) {
        LoginResponse.LoginResult r = authService.register(request.getUsername(), request.getPassword());
        return Result.success(new LoginResponse(r.id(), r.username(), r.role(), r.token()));
    }
}
