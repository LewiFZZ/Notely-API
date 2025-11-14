package com.notely.api.notely.service.interfaces;

import java.util.List;
import java.util.Optional;

import com.notely.api.notely.dto.NoteDTO;

public interface NoteServiceI {
    List<NoteDTO> getAllNotes();
    Optional<NoteDTO> getNoteById(Long id);
    NoteDTO createNote(NoteDTO noteDTO);
    Optional<NoteDTO> updateNote(Long id, NoteDTO noteDTO);
    boolean deleteNote(Long id);
}
