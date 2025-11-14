package com.notely.api.notely.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.notely.api.notely.entity.Note;
import com.notely.api.notely.entity.AppUser;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long>{
    List<Note> findByUser(AppUser user);
    Optional<Note> findByIdAndUser(Long id, AppUser user);
    List<Note> findByUserAndIsActiveTrue(AppUser user);
}
