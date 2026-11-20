// backend/src/main/java/com/campus/strayanimal/repository/UserRepository.java
package com.campus.strayanimal.repository;

import com.campus.strayanimal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}