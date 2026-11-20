// backend/src/main/java/com/campus/strayanimal/service/AuthService.java
package com.campus.strayanimal.service;

import com.campus.strayanimal.entity.User;

public interface AuthService {
    User login(String username, String password);
    User register(String username, String password);
}