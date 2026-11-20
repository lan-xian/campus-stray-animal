// backend/src/main/java/com/campus/strayanimal/dto/LoginRequest.java
package com.campus.strayanimal.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}