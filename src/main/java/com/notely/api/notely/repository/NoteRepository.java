package com.notely.api.notely.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.notely.api.notely.entity.Note;

public interface NoteRepository extends JpaRepository<Note, Long>{}
