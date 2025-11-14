package com.notely.api.notely.entity;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import com.notely.api.notely.listener.EncryptionListener;

@Entity
@Table(name = "category")
@EntityListeners(EncryptionListener.class)
public class Category {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String name;
    
    // 1:N to Note
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private Set<Note> notes;

    // 1:N to AppUser
    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser user;

    private Boolean isActive;

    // Getters and Setters
     public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

     public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Note> getNotes() {
        return notes;
    }

    public void setNotes(Set<Note> notes) {
        this.notes = notes;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public Boolean getActive(){
        return isActive;
    }

    public void setActive(Boolean isActive){
        this.isActive = isActive;
    }



}
