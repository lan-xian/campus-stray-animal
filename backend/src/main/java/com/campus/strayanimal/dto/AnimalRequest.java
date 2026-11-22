package com.campus.strayanimal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AnimalRequest {

    @NotBlank(message = "不能为空")
    @Size(max = 50, message = "长度不能超过 50")
    private String name;

    @NotBlank(message = "不能为空")
    @Size(max = 10, message = "长度不能超过 10")
    private String type;

    @Size(max = 100, message = "长度不能超过 100")
    private String area;
}
