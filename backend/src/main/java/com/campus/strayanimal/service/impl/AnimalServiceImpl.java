package com.campus.strayanimal.service.impl;

import com.campus.strayanimal.common.BusinessException;
import com.campus.strayanimal.entity.Animal;
import com.campus.strayanimal.repository.AnimalRepository;
import com.campus.strayanimal.service.AnimalService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Service
public class AnimalServiceImpl implements AnimalService {

    /** 允许上传的图片扩展名白名单 */
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png", "gif", "webp");

    private final AnimalRepository animalRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public AnimalServiceImpl(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    @Override
    @Transactional
    public Animal create(Animal animal, MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            animal.setCoverImage(saveFile(file));
        }
        animal.setCreateTime(LocalDateTime.now());
        animal.setUpdateTime(LocalDateTime.now());
        try {
            return animalRepository.save(animal);
        } catch (DataIntegrityViolationException e) {
            throw BusinessException.conflict("相同名字和类型的动物已存在");
        }
    }

    @Override
    @Transactional
    public Animal update(Long id, Animal updateData, MultipartFile file) {
        Animal animal = animalRepository.findById(id)
                .orElseThrow(() -> BusinessException.notFound("动物档案不存在"));
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
            throw BusinessException.conflict("相同名字和类型的动物已存在");
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!animalRepository.existsById(id)) {
            throw BusinessException.notFound("动物档案不存在");
        }
        animalRepository.deleteById(id);
    }

    @Override
    public Animal getById(Long id) {
        return animalRepository.findById(id)
                .orElseThrow(() -> BusinessException.notFound("动物档案不存在"));
    }

    @Override
    public Page<Animal> list(String name, String type, int page, int size) {
        // 限制单页大小，防止恶意大分页拖垮数据库
        size = Math.min(Math.max(size, 1), 50);
        PageRequest pageRequest = PageRequest.of(Math.max(page, 0), size,
                Sort.by(Sort.Direction.DESC, "createTime"));
        return animalRepository.findByFilters(name, type, pageRequest);
    }

    private String saveFile(MultipartFile file) {
        // 只保留文件名部分，防止路径穿越；扩展名白名单校验
        String original = Paths.get(file.getOriginalFilename()).getFileName().toString();
        String extension = original.contains(".")
                ? original.substring(original.lastIndexOf('.') + 1).toLowerCase()
                : "";
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw BusinessException.badRequest("仅支持图片格式: " + String.join("/", ALLOWED_EXTENSIONS));
        }
        try {
            Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(uploadPath);
            // UUID 重命名防覆盖与防猜測
            String filename = UUID.randomUUID() + "." + extension;
            Path targetPath = uploadPath.resolve(filename).normalize();
            // 双重确认目标仍在上传目录内
            if (!targetPath.startsWith(uploadPath)) {
                throw BusinessException.badRequest("非法的文件路径");
            }
            file.transferTo(targetPath);
            return "/uploads/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("文件上传失败", e);
        }
    }
}
