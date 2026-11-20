// backend/src/main/java/com/campus/strayanimal/dto/CheckInRequest.java
package com.campus.strayanimal.dto;

import lombok.Data;

@Data
public class CheckInRequest {
    private Long animalId;
    private Long userId;
    private String content;
}