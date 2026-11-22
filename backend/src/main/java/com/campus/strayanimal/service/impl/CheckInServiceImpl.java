// backend/src/main/java/com/campus/strayanimal/service/impl/CheckInServiceImpl.java
package com.campus.strayanimal.service.impl;

import com.campus.strayanimal.common.BusinessException;
import com.campus.strayanimal.entity.Animal;
import com.campus.strayanimal.entity.CheckIn;
import com.campus.strayanimal.entity.User;
import com.campus.strayanimal.repository.AnimalRepository;
import com.campus.strayanimal.repository.CheckInRepository;
import com.campus.strayanimal.repository.UserRepository;
import com.campus.strayanimal.service.CheckInService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CheckInServiceImpl implements CheckInService {

    private final CheckInRepository checkInRepository;
    private final AnimalRepository animalRepository;
    private final UserRepository userRepository;

    public CheckInServiceImpl(CheckInRepository checkInRepository, AnimalRepository animalRepository, UserRepository userRepository) {
        this.checkInRepository = checkInRepository;
        this.animalRepository = animalRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public CheckIn create(Long animalId, Long userId, String content) {
        Animal animal = animalRepository.findById(animalId)
                .orElseThrow(() -> BusinessException.notFound("动物不存在"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> BusinessException.notFound("用户不存在"));
        CheckIn checkIn = new CheckIn();
        checkIn.setAnimal(animal);
        checkIn.setUser(user);
        checkIn.setContent(content);
        return checkInRepository.save(checkIn);
    }

    @Override
    public List<CheckIn> listByAnimalId(Long animalId) {
        return checkInRepository.findByAnimalIdOrderByCreateTimeDesc(animalId);
    }
}