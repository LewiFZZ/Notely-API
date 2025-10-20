package com.notely.api.notely.dto;

import java.time.LocalDate;

import com.notely.api.notely.entity.AppUser;

public class AppUserDTO {
    private Long uid;
    private String name;
    private String email;
    private LocalDate createdAt;

    public AppUserDTO(){}

    public AppUserDTO(AppUser user) {
        //TODO Auto-generated constructor stub
    }

    // Getters & Setters
    public Long getUid() { return uid; }
    public void setUid(Long uid) { this.uid = uid; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDate getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }
}
