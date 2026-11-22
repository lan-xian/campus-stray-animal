package com.campus.strayanimal.dto;

import lombok.Data;

@Data
public class LoginResponse {
    private Long id;
    private String username;
    private String role;
    private String token;

    public LoginResponse(Long id, String username, String role, String token) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.token = token;
    }

    /** Service 层返回的登录结果（含 JWT） */
    public record LoginResult(Long id, String username, String role, String token) {}
}
