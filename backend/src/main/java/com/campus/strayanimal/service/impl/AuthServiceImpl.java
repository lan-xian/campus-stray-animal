package com.campus.strayanimal.service.impl;

import com.campus.strayanimal.common.BusinessException;
import com.campus.strayanimal.entity.User;
import com.campus.strayanimal.repository.UserRepository;
import com.campus.strayanimal.security.JwtUtil;
import com.campus.strayanimal.service.AuthService;
import com.campus.strayanimal.dto.LoginResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public LoginResponse.LoginResult login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> BusinessException.unauthorized("用户名或密码错误"));
        // BCrypt 比对：即使密码正确也走 encoder.matches，避免时序差异暴露用户是否存在
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw BusinessException.unauthorized("用户名或密码错误");
        }
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
        return new LoginResponse.LoginResult(user.getId(), user.getUsername(), user.getRole(), token);
    }

    @Override
    public LoginResponse.LoginResult register(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            throw BusinessException.badRequest("用户名不能为空");
        }
        if (password == null || password.trim().isEmpty()) {
            throw BusinessException.badRequest("密码不能为空");
        }
        User user = new User();
        user.setUsername(username.trim());
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("USER");
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw BusinessException.conflict("用户名已存在");
        }
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
        return new LoginResponse.LoginResult(user.getId(), user.getUsername(), user.getRole(), token);
    }
}
