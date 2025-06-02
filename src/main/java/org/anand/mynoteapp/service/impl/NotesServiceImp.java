package org.anand.mynoteapp.service.impl;

import org.anand.mynoteapp.dto.NotesDto;
import org.anand.mynoteapp.entity.Notes;
import org.anand.mynoteapp.exception.ResourceNotFoundException;
import org.anand.mynoteapp.repository.CategoryRepository;
import org.anand.mynoteapp.repository.NotesRepository;
import org.anand.mynoteapp.service.NotesService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import java.util.List;

@Service
public class NotesServiceImp implements NotesService {

    @Autowired
    private NotesRepository notesRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Boolean saveNotes(NotesDto notesDto) throws Exception {
        // before saving the notes check category present or not
        checkCategoryExist(notesDto.getCategory());
        Notes notes = modelMapper.map(notesDto, Notes.class);
        Notes saveNote = notesRepository.save(notes);
        if (!ObjectUtils.isEmpty(saveNote)){
            return true;
        }
        return false;
    }

    private void checkCategoryExist(NotesDto.CategoryDto categoryDto) throws Exception {
        categoryRepository.findById(categoryDto.getId()).orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }

    @Override
    public List<NotesDto> getAllNotes() {
        return  notesRepository.findAll().stream().map(note -> modelMapper.map(note,NotesDto.class)).toList();
    }
}
