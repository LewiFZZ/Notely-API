package com.notely.api.notely.service.interfaces;

import java.util.List;

import com.notely.api.notely.dto.NoteDTO;

public interface NoteServiceI {
    List<NoteDTO> getAllNotes();
    NoteDTO getNoteById(Long id);
}
