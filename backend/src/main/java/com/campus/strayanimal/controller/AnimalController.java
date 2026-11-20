// backend/src/main/java/com/campus/strayanimal/controller/AnimalController.java
package com.campus.strayanimal.controller;

import com.campus.strayanimal.common.Result;
import com.campus.strayanimal.dto.AnimalRequest;
import com.campus.strayanimal.entity.Animal;
import com.campus.strayanimal.service.AnimalService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/animals")
public class AnimalController {

    private final AnimalService animalService;

    public AnimalController(AnimalService animalService) {
        this.animalService = animalService;
    }

    @PostMapping
    public Result<Animal> create(@RequestPart("data") AnimalRequest request,
                                 @RequestPart(value = "file", required = false) MultipartFile file) {
        Animal animal = new Animal();
        animal.setName(request.getName());
        animal.setType(request.getType());
        animal.setArea(request.getArea());
        return Result.success(animalService.create(animal, file));
    }

    @PutMapping("/{id}")
    public Result<Animal> update(@PathVariable Long id,
                                 @RequestPart("data") AnimalRequest request,
                                 @RequestPart(value = "file", required = false) MultipartFile file) {
        Animal animal = new Animal();
        animal.setName(request.getName());
        animal.setType(request.getType());
        animal.setArea(request.getArea());
        return Result.success(animalService.update(id, animal, file));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        animalService.delete(id);
        return Result.success(null);
    }

    @GetMapping("/{id}")
    public Result<Animal> getById(@PathVariable Long id) {
        return Result.success(animalService.getById(id));
    }

    @GetMapping
    public Result<Page<Animal>> list(@RequestParam(required = false) String name,
                                     @RequestParam(required = false) String type,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size) {
        return Result.success(animalService.list(name, type, page, size));
    }
}