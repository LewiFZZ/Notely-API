package com.notely.api.notely.entity;

import java.time.LocalDate;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;

// import org.hibernate.annotations.ManyToAny;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "app_user")
public class AppUser {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long uid;

    private String name;
    private String email;
    private LocalDate created_at;

    // 1:N with Category
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Category> categories;

    // 1:N with Note
    @OneToMany(mappedBy = "user")
    @JsonBackReference
    private Set<Note> notes;

    @Column(name = "firebase_auth_uid", unique = true)
    private String firebase_auth_uid;

    private Boolean isActive;


    // Getters and Setters
    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDate created_at) {
        this.created_at = created_at;
    }

    public Set<Note> getNotes() {
        return notes;
    }

    public void setNotes(Set<Note> notes) {
        this.notes = notes;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public Boolean getActive(){
        return isActive;
    }

    public void setActive(Boolean isActive){
        this.isActive = isActive;
    }

    public String getFirebaseUid(){
        return firebase_auth_uid;
    }

    public void setFirebaseUid(String firebaseuid){
        this.firebase_auth_uid = firebaseuid;
    }

}
