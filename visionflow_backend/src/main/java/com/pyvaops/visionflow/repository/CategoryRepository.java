package com.pyvaops.visionflow.repository;

import com.pyvaops.visionflow.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// 🌟 PK 타입 제네릭을 Integer로 변경합니다.
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Optional<Category> findByCategoryName(String categoryName);
}