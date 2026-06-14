package com.pyvaops.visionflow.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Integer id; // 🌟 Long에서 Integer로 변경하여 MySQL INT 타입과 일치시킵니다.

    @Column(name = "category_name", nullable = false, unique = true)
    private String categoryName;

    public Category() {}

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer id() { return id; } // 🌟 리턴 타입 Integer로 수정
    public String categoryName() { return categoryName; }
}