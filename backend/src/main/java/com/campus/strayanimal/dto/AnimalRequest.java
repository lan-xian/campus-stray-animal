// backend/src/main/java/com/campus/strayanimal/dto/AnimalRequest.java
package com.campus.strayanimal.dto;

import lombok.Data;

@Data
public class AnimalRequest {
    private String name;
    private String type;
    private String area;
}