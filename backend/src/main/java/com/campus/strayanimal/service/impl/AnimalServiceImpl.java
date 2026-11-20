// backend/src/main/java/com/campus/strayanimal/service/impl/AnimalServiceImpl.java
package com.campus.strayanimal.service.impl;

import com.campus.strayanimal.entity.Animal;
import com.campus.strayanimal.repository.AnimalRepository;
import com.campus.strayanimal.service.AnimalService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AnimalServiceImpl implements AnimalService {

    private final AnimalRepository animalRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public AnimalServiceImpl(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    @Override
    public Animal create(Animal animal, MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            animal.setCoverImage(saveFile(file));
        }
        animal.setCreateTime(LocalDateTime.now());
        animal.setUpdateTime(LocalDateTime.now());
        try {
            return animalRepository.save(animal);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("相同名字和类型的动物已存在");
        }
    }

    @Override
    public Animal update(Long id, Animal updateData, MultipartFile file) {
        Animal animal = animalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("动物档案不存在"));
        animal.setName(updateData.getName());
        animal.setType(updateData.getType());
        animal.setArea(updateData.getArea());
        if (file != null && !file.isEmpty()) {
            animal.setCoverImage(saveFile(file));
        }
        animal.setUpdateTime(LocalDateTime.now());
        try {
            return animalRepository.save(animal);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("相同名字和类型的动物已存在");
        }
    }

    @Override
    public void delete(Long id) {
        animalRepository.deleteById(id);
    }

    @Override
    public Animal getById(Long id) {
        return animalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("动物档案不存在"));
    }

    @Override
    public Page<Animal> list(String name, String type, int page, int size) {
        return animalRepository.findByFilters(name, type, PageRequest.of(page, size));
    }

    private String saveFile(MultipartFile file) {
        try {
            Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(uploadPath);
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path targetPath = uploadPath.resolve(filename);
            file.transferTo(targetPath.toFile());
            return "/uploads/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("文件上传失败: " + e.getMessage());
        }
    }
}