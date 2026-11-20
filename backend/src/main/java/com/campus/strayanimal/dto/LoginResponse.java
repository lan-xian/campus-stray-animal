// backend/src/main/java/com/campus/strayanimal/dto/LoginResponse.java
package com.campus.strayanimal.dto;

import lombok.Data;

@Data
public class LoginResponse {
    private Long id;
    private String username;
    private String role;

    public LoginResponse(Long id, String username, String role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }
}