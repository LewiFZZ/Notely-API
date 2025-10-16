package com.notely.api.notely.dto;

public class NoteDTO {
    private Long id;
    private String name;
    private String content;
    private CategoryDTO  categoryId;
    private AppUserDTO  userId;


    public NoteDTO(){}

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public CategoryDTO getCategoryId() { return categoryId; }
    public void setCategoryId(CategoryDTO categoryId) { this.categoryId = categoryId; }

    public AppUserDTO getUserId() { return userId; }
    public void setUserId(AppUserDTO userId) { this.userId = userId; }
}
