package com.notely.api.notely.service.classes;

import java.util.List;
import java.util.stream.Collectors;

import com.notely.api.notely.dto.AppUserDTO;
import com.notely.api.notely.dto.CategoryDTO;
import com.notely.api.notely.dto.NoteDTO;
import com.notely.api.notely.entity.Note;
import com.notely.api.notely.repository.AppUserRepository;
import com.notely.api.notely.repository.CategoryRepository;
import com.notely.api.notely.repository.NoteRepository;
import com.notely.api.notely.service.interfaces.NoteServiceI;

public class NoteService implements NoteServiceI{

    private final NoteRepository noteRepository;
    // private final AppUserRepository userRepository;
    // private final CategoryRepository categoryRepository;

    public NoteService(NoteRepository noteRepository, AppUserRepository userRepository, CategoryRepository categoryRepository){
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<NoteDTO> getAllNotes(){
   
        try {
            return noteRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        } catch (Exception e) {
           System.err.println("Error obtaining notes: " + e.getMessage());
           return List.of();
        }
    }

    @Override
    public NoteDTO getNoteById(Long id){

        try {
            return noteRepository.findAllById(id).map(this::toDTO).orElse(null);

        } catch (Exception e) {
            System.err.println("Error obtaining the desired note with ID" + id +": " + e.getMessage());
        }
    }


    private NoteDTO toDTO(Note note) {
        NoteDTO dto = new NoteDTO();
        dto.setId(note.getId());
        dto.setName(note.getName());
        dto.setContent(note.getContent());
        dto.setUserId(new AppUserDTO(note.getUser()));       
        dto.setCategoryId(new CategoryDTO(note.getCategory()));
        return dto;
    }

}
