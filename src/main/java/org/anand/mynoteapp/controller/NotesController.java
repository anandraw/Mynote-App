package org.anand.mynoteapp.controller;

import org.anand.mynoteapp.dto.NotesDto;
import org.anand.mynoteapp.service.NotesService;
import org.anand.mynoteapp.utils.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/notes")
public class NotesController{
    
    @Autowired
    private NotesService notesService;

    @PostMapping("/")
    public ResponseEntity<?> saveNote(@RequestBody NotesDto notesDto) throws Exception {
        Boolean saveNote = notesService.saveNotes(notesDto);
        if (saveNote) {
            return CommonUtil.createBuildResponseMessage("notes save successfully",notesDto, HttpStatus.OK);
        }
        return CommonUtil.createErrorResponseMessage("notes not saved",HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @GetMapping("/")
    public ResponseEntity<?> getAllNotes() {
        List<NotesDto> notes = notesService.getAllNotes();
        if (CollectionUtils.isEmpty(notes)) {
            return ResponseEntity.noContent().build();
        }
        return CommonUtil.createBuildResponse(notes, HttpStatus.OK);
    }

}
