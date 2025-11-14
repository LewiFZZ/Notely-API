package com.notely.api.notely.service.classes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.notely.api.notely.dto.AppUserDTO;
import com.notely.api.notely.dto.CategoryDTO;
import com.notely.api.notely.dto.NoteDTO;
import com.notely.api.notely.entity.Note;
import com.notely.api.notely.entity.Category;
import com.notely.api.notely.entity.AppUser;
import com.notely.api.notely.repository.NoteRepository;
import com.notely.api.notely.repository.CategoryRepository;
import com.notely.api.notely.service.interfaces.NoteServiceI;
import com.notely.api.notely.util.UserContext;

@Service
public class NoteService implements NoteServiceI{

    private final NoteRepository noteRepository;
    private final CategoryRepository categoryRepository;

    public NoteService(NoteRepository noteRepository, CategoryRepository categoryRepository){
        this.noteRepository = noteRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<NoteDTO> getAllNotes(){
        AppUser currentUser = UserContext.getCurrentUser();
        if (currentUser == null) {
            throw new SecurityException("User not authenticated");
        }
        
        try {
            return noteRepository.findByUserAndIsActiveTrue(currentUser)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        } catch (Exception e) {
           System.err.println("Error obtaining notes: " + e.getMessage());
           return List.of();
        }
    }

    @Override
    public Optional<NoteDTO> getNoteById(Long id){
        AppUser currentUser = UserContext.getCurrentUser();
        if (currentUser == null) {
            throw new SecurityException("User not authenticated");
        }

        try {
            return noteRepository.findByIdAndUser(id, currentUser)
                .map(this::toDTO);
        } catch (Exception e) {
            System.err.println("Error obtaining the desired note with ID " + id + ": " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public NoteDTO createNote(NoteDTO noteDTO) {
        AppUser currentUser = UserContext.getCurrentUser();
        if (currentUser == null) {
            throw new SecurityException("User not authenticated");
        }

        Note note = new Note();
        note.setName(noteDTO.getName());
        note.setContent(noteDTO.getContent());
        note.setUser(currentUser);
        note.setActive(true);
        
        // Set category if categoryId is provided
        if (noteDTO.getCategoryId() != null) {
            Optional<Category> categoryOpt = categoryRepository.findByIdAndUser(
                noteDTO.getCategoryId(), currentUser);
            if (categoryOpt.isPresent()) {
                note.setCategory(categoryOpt.get());
            } else {
                throw new IllegalArgumentException(
                    "Category with ID " + noteDTO.getCategoryId() + " not found or doesn't belong to user");
            }
        }
        
        Note savedNote = noteRepository.save(note);
        return toDTO(savedNote);
    }

    @Override
    @Transactional
    public Optional<NoteDTO> updateNote(Long id, NoteDTO noteDTO) {
        AppUser currentUser = UserContext.getCurrentUser();
        if (currentUser == null) {
            throw new SecurityException("User not authenticated");
        }

        Optional<Note> noteOpt = noteRepository.findByIdAndUser(id, currentUser);
        if (noteOpt.isEmpty()) {
            return Optional.empty();
        }

        Note note = noteOpt.get();
        note.setName(noteDTO.getName());
        note.setContent(noteDTO.getContent());
        
        // Update category if categoryId is provided
        if (noteDTO.getCategoryId() != null) {
            Optional<Category> categoryOpt = categoryRepository.findByIdAndUser(
                noteDTO.getCategoryId(), currentUser);
            if (categoryOpt.isPresent()) {
                note.setCategory(categoryOpt.get());
            } else {
                throw new IllegalArgumentException(
                    "Category with ID " + noteDTO.getCategoryId() + " not found or doesn't belong to user");
            }
        } else {
            // If categoryId is null, remove the category
            note.setCategory(null);
        }
        
        Note updatedNote = noteRepository.save(note);
        return Optional.of(toDTO(updatedNote));
    }

    @Override
    @Transactional
    public boolean deleteNote(Long id) {
        AppUser currentUser = UserContext.getCurrentUser();
        if (currentUser == null) {
            throw new SecurityException("User not authenticated");
        }

        Optional<Note> noteOpt = noteRepository.findByIdAndUser(id, currentUser);
        if (noteOpt.isEmpty()) {
            return false;
        }

        Note note = noteOpt.get();
        note.setActive(false); // Soft delete
        noteRepository.save(note);
        return true;
    }

    private NoteDTO toDTO(Note note) {
        NoteDTO dto = new NoteDTO();
        dto.setId(note.getId());
        dto.setName(note.getName());
        dto.setContent(note.getContent());
        if (note.getUser() != null) {
            AppUserDTO userDTO = new AppUserDTO();
            userDTO.setUid(note.getUser().getUid());
            userDTO.setName(note.getUser().getName());
            userDTO.setEmail(note.getUser().getEmail());
            dto.setUserId(userDTO);
        }

        if (note.getCategory() != null) {
            CategoryDTO catDTO = new CategoryDTO();
            catDTO.setId(note.getCategory().getId());
            catDTO.setName(note.getCategory().getName());
            catDTO.setUserId(note.getUser().getUid());
            dto.setCategory(catDTO);
            dto.setCategoryId(note.getCategory().getId()); // Also set for convenience
        }
        return dto;
    }

}
