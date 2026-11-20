// backend/src/main/java/com/campus/strayanimal/controller/CheckInController.java
package com.campus.strayanimal.controller;

import com.campus.strayanimal.common.Result;
import com.campus.strayanimal.dto.CheckInRequest;
import com.campus.strayanimal.entity.CheckIn;
import com.campus.strayanimal.service.CheckInService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CheckInController {

    private final CheckInService checkInService;

    public CheckInController(CheckInService checkInService) {
        this.checkInService = checkInService;
    }

    @PostMapping("/check-ins")
    public Result<CheckIn> create(@RequestBody CheckInRequest request) {
        return Result.success(checkInService.create(request.getAnimalId(), request.getUserId(), request.getContent()));
    }

    @GetMapping("/animals/{animalId}/check-ins")
    public Result<List<CheckIn>> listByAnimalId(@PathVariable Long animalId) {
        return Result.success(checkInService.listByAnimalId(animalId));
    }
}