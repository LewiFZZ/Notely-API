package com.notely.api.notely.dto;

import com.notely.api.notely.entity.Category;

public class CategoryDTO {
    private Long id;
    private String name;
    private Long userId;

    public CategoryDTO(){}

    public CategoryDTO(Category category) {
        //TODO Auto-generated constructor stub
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}
