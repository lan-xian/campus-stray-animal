// backend/src/main/java/com/campus/strayanimal/service/CheckInService.java
package com.campus.strayanimal.service;

import com.campus.strayanimal.entity.CheckIn;
import java.util.List;

public interface CheckInService {
    CheckIn create(Long animalId, Long userId, String content);
    List<CheckIn> listByAnimalId(Long animalId);
}