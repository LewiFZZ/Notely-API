package com.notely.api.notely.service.classes;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.notely.api.notely.dto.AppUserDTO;
import com.notely.api.notely.dto.CategoryDTO;
import com.notely.api.notely.dto.NoteDTO;
import com.notely.api.notely.entity.Note;
import com.notely.api.notely.repository.NoteRepository;
import com.notely.api.notely.service.interfaces.NoteServiceI;

@Service
public class NoteService implements NoteServiceI{

    private final NoteRepository noteRepository;


    public NoteService(NoteRepository noteRepository){
        this.noteRepository = noteRepository;
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
            return noteRepository.findById(id).map(this::toDTO).orElse(null);

        } catch (Exception e) {
            System.err.println("Error obtaining the desired note with ID" + id +": " + e.getMessage());
            return null;
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
