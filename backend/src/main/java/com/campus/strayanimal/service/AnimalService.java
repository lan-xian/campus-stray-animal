// backend/src/main/java/com/campus/strayanimal/service/AnimalService.java
package com.campus.strayanimal.service;

import com.campus.strayanimal.entity.Animal;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface AnimalService {
    Animal create(Animal animal, MultipartFile file);
    Animal update(Long id, Animal animal, MultipartFile file);
    void delete(Long id);
    Animal getById(Long id);
    Page<Animal> list(String name, String type, int page, int size);
}