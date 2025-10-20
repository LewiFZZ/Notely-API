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
            dto.setCategoryId(catDTO);
        }
        return dto;
    }

}
