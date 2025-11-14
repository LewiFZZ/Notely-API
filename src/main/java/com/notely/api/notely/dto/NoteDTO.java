package com.notely.api.notely.dto;

public class NoteDTO {
    private Long id;
    private String name;
    private String content;
    private Long categoryId;  // Changed from CategoryDTO to Long for easier use
    private CategoryDTO category;  // For response (read-only)
    private AppUserDTO user;

    public NoteDTO(){}

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    // For request: Accept categoryId as Long
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    // For response: Return full CategoryDTO
    public CategoryDTO getCategory() { return category; }
    public void setCategory(CategoryDTO category) { this.category = category; }

    public AppUserDTO getUserId() { return user; }
    public void setUserId(AppUserDTO user) { this.user = user; }
}
