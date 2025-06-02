package org.anand.mynoteapp.service;

import org.anand.mynoteapp.dto.NotesDto;
import java.util.List;

public interface NotesService {

    public Boolean saveNotes(NotesDto notesDto) throws Exception;
    public List<NotesDto> getAllNotes();
}
