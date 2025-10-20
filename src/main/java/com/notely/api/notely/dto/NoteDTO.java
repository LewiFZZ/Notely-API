package com.notely.api.notely.dto;

public class NoteDTO {
    private Long id;
    private String name;
    private String content;
    private CategoryDTO  category;
    private AppUserDTO  user;


    public NoteDTO(){}

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public CategoryDTO getCategoryId() { return category; }
    public void setCategoryId(CategoryDTO category) { this.category = category; }

    public AppUserDTO getUserId() { return user; }
    public void setUserId(AppUserDTO user) { this.user = user; }
}
