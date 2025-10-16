package com.notely.api.notely.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.notely.api.notely.dto.NoteDTO;
import com.notely.api.notely.service.classes.NoteService;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestParam;


@RestController()
@CrossOrigin("**")
@RequestMapping("/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService){
        this.noteService = noteService;
    }

    /**
     * Retrieves all notes.
     *
     * @return ResponseEntity containing a list of NoteDTO objects.
     *
     * @throws Exception if an error occurs while fetching notes.
     *
     * @swagger
     * @operation summary="Get all notes"
     * @response 200 description="Successfully retrieved list of notes"
     * @response 500 description="Internal server error"
     */
    @GetMapping("/all")
    public ResponseEntity<List<NoteDTO>> getAllNotes() {
        try {
            List<NoteDTO> notes = noteService.getAllNotes();
            return ResponseEntity.ok(notes);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
}
