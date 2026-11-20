// backend/src/main/java/com/campus/strayanimal/service/impl/AuthServiceImpl.java
package com.campus.strayanimal.service.impl;

import com.campus.strayanimal.entity.User;
import com.campus.strayanimal.repository.UserRepository;
import com.campus.strayanimal.service.AuthService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户名或密码错误"));
        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("用户名或密码错误");
        }
        return user;
    }

    @Override
    public User register(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            throw new RuntimeException("用户名不能为空");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new RuntimeException("密码不能为空");
        }
        User user = new User();
        user.setUsername(username.trim());
        user.setPassword(password);
        user.setRole("USER");
        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("用户名已存在");
        }
    }
}