package com.campus.strayanimal.service;

import com.campus.strayanimal.dto.LoginResponse;

public interface AuthService {
    LoginResponse.LoginResult login(String username, String password);
    LoginResponse.LoginResult register(String username, String password);
}
