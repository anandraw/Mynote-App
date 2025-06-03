package org.anand.mynoteapp.service;

import org.anand.mynoteapp.dto.NotesDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface NotesService {

    public Boolean saveNotes(String notes, MultipartFile file) throws Exception;
    public List<NotesDto> getAllNotes();
}
