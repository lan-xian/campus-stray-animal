// backend/src/main/java/com/campus/strayanimal/repository/CheckInRepository.java
package com.campus.strayanimal.repository;

import com.campus.strayanimal.entity.CheckIn;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CheckInRepository extends JpaRepository<CheckIn, Long> {
    List<CheckIn> findByAnimalIdOrderByCreateTimeDesc(Long animalId);
}