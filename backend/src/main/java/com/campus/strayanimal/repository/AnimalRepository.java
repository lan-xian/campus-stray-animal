// backend/src/main/java/com/campus/strayanimal/repository/AnimalRepository.java
package com.campus.strayanimal.repository;

import com.campus.strayanimal.entity.Animal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AnimalRepository extends JpaRepository<Animal, Long> {
    @Query("SELECT a FROM Animal a WHERE " +
            "(:name IS NULL OR :name = '' OR a.name LIKE %:name%) AND " +
            "(:type IS NULL OR :type = '' OR a.type = :type)")
    Page<Animal> findByFilters(@Param("name") String name, @Param("type") String type, Pageable pageable);
}