package org.anand.mynoteapp.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.anand.mynoteapp.dto.FavouriteNoteDto;
import org.anand.mynoteapp.dto.NotesDto;
import org.anand.mynoteapp.dto.NotesResponse;
import org.anand.mynoteapp.entity.FavouriteNote;
import org.anand.mynoteapp.entity.FileDetails;
import org.anand.mynoteapp.entity.Notes;
import org.anand.mynoteapp.exception.ResourceNotFoundException;
import org.anand.mynoteapp.repository.CategoryRepository;
import org.anand.mynoteapp.repository.FavouriteNoteRepository;
import org.anand.mynoteapp.repository.FileRepository;
import org.anand.mynoteapp.repository.NotesRepository;
import org.anand.mynoteapp.service.NotesService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.FilenameUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class NotesServiceImp implements NotesService {

    @Autowired
    private NotesRepository notesRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private FileRepository fileRepository;

    @Value("${file.upload.path}")
    private String uploadpath;

    @Autowired
    private FavouriteNoteRepository favouriteNoteRepository;


    @Override
    public Boolean saveNotes(String notes, MultipartFile file) throws Exception {
        ObjectMapper ob = new ObjectMapper();
        NotesDto notesDto = ob.readValue(notes, NotesDto.class);

        notesDto.setIsDeleted(false);
        notesDto.setDeletedOn(null);

        //update notes only if id is given in request
        if (!ObjectUtils.isEmpty(notesDto.getId())) {
            updateNotes(notesDto,file);
        }

        // Category validation
        checkCategoryExist(notesDto.getCategory());

        Notes notesMap = modelMapper.map(notesDto, Notes.class);

        // Save file details if provided
        FileDetails fileDetails = saveFileDetails(file);
        notesMap.setFileDetails(fileDetails);

        // Save the note
        Notes saveNotes = notesRepository.save(notesMap);
        return !ObjectUtils.isEmpty(saveNotes);
    }

    private void updateNotes(NotesDto notesDto, MultipartFile file) {
        Notes existNote = notesRepository.findById(notesDto.getId()).
                orElseThrow(() -> new ResourceNotFoundException("Invalid note id"));
        //user not choose file that time
        if (ObjectUtils.isEmpty(file)) {
            notesDto.setFileDetails(modelMapper.map(existNote.getFileDetails(), NotesDto.FilesDto.class));
        }

    }

    private FileDetails saveFileDetails(MultipartFile file) throws IOException {
        // Corrected condition
        if (file != null && !file.isEmpty()) {

            String originalFilename = file.getOriginalFilename();
            String extension = FilenameUtils.getExtension(originalFilename);

            List<String> allowedExtensions = Arrays.asList("pdf", "xlsx", "jpg", "png");
            if (!allowedExtensions.contains(extension)) {
                throw new IllegalArgumentException("Invalid file format! Allowed: pdf, xlsx, jpg, png");
            }

            String rndString = UUID.randomUUID().toString();
            String uploadFileName = rndString + "." + extension;

            // Ensure upload folder exists
            File uploadFolder = new File(uploadpath);
            if (!uploadFolder.exists()) {
                uploadFolder.mkdirs(); // creates the full directory structure if not present
            }

            // Define the full path to store file
            String storePath = uploadpath + File.separator + uploadFileName;

            // Save file
            long upload = Files.copy(file.getInputStream(), Paths.get(storePath));
            if (upload > 0) {
                FileDetails fileDetails = new FileDetails();
                fileDetails.setOriginalFileName(originalFilename);
                fileDetails.setDisplayFileName(getDisplayName(originalFilename));
                fileDetails.setUploadFileName(uploadFileName);
                fileDetails.setFileSize(file.getSize());
                fileDetails.setPath(storePath);

                return fileRepository.save(fileDetails);
            }
        }
        return null;
    }
    private String getDisplayName(String originalFilename) {
        // java_programming_tutorials.pdf
        String extension = FilenameUtils.getExtension(originalFilename); //pdf
        String fileName = FilenameUtils.removeExtension(originalFilename);//java_programming_tutorials

        if (fileName.length() > 8) {
            fileName = fileName.substring(0, 7);
        }
        fileName = fileName + "." + extension;
        return fileName;
    }

    private void checkCategoryExist(NotesDto.CategoryDto categoryDto) throws Exception {
        categoryRepository.findById(categoryDto.getId()).orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }


    @Override
    public List<NotesDto> getAllNotes() {
        return  notesRepository.findAll().stream().map(note -> modelMapper.map(note,NotesDto.class)).toList();
    }

    @Override
    public byte[] downloadFile(FileDetails fileDtls) throws Exception {
        InputStream io = new FileInputStream(fileDtls.getPath());
        return StreamUtils.copyToByteArray(io);
    }

    @Override
    public FileDetails getFileDetails(Integer id) throws Exception {
        FileDetails fileDetails=fileRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("File is not available"));
        return fileDetails;
    }

    @Override
    public NotesResponse getAllNotesByUser(Integer userId, Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Notes> pageNotes = notesRepository.findByCreatedBy(userId, pageable);
        List<NotesDto> notes = pageNotes.stream().map(n -> modelMapper.map(n,NotesDto.class)).toList();
        NotesResponse notesResponse = NotesResponse.builder().notes(notes).pageNo(pageNotes.getNumber())
                .pageSize(pageNotes.getSize()).totalElements(pageNotes.getTotalElements()).
                totalPages(pageNotes.getTotalPages()).isFirst(pageNotes.isFirst())
                .isLast(pageNotes.isLast()).build();

        return notesResponse;
    }

    @Override
    public void softDeleteNotes(Integer id) throws Exception {
        Notes notes = notesRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Note is not available"));
        notes.setIsDeleted(true);
        notes.setDeletedOn(null);
        notesRepository.save(notes);
    }

    @Override
    public void restoreNotes(Integer id) throws Exception {
        Notes notes = notesRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Note is not available"));
        notes.setIsDeleted(false);
        notes.setDeletedOn(null);
        notesRepository.save(notes);
    }

    @Override
    public List<NotesDto> getUserRecycleBinNotes(Integer userId) {
        List<Notes> recycleNotes = notesRepository.findByCreatedByAndIsDeletedTrue(userId);
        List<NotesDto> notesDtoList = recycleNotes.stream()
                .map(note -> modelMapper.map(note, NotesDto.class)).toList();
        return notesDtoList;
    }

    @Override
    public void hardDeleteNotes(Integer id) throws Exception {
        Notes notes = notesRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Notes not found"));
        if (notes.getIsDeleted()) {
            notesRepository.delete(notes);
        } else {
            throw new IllegalArgumentException("Sorry You cant hard delete Directly");
        }
    }

    @Override
    public void emptyRecycleBin(int userId) {
        List<Notes> recycleNotes = notesRepository.findByCreatedByAndIsDeletedTrue(userId);
        if (!CollectionUtils.isEmpty(recycleNotes)) {
            notesRepository.deleteAll(recycleNotes);
        }
    }

    @Override
    public void favoriteNotes(Integer noteId) throws Exception {
        Integer userId=1;
        Notes notes = notesRepository.findById(noteId).
                orElseThrow(() -> new ResourceNotFoundException("Note not found with this :" + noteId));
        FavouriteNote favouriteNote = FavouriteNote.builder().note(notes).userId(userId).build();
        favouriteNoteRepository.save(favouriteNote);
    }

    @Override
    public void unFavoriteNotes(Integer noteId) throws Exception {
        FavouriteNote favouriteNote = favouriteNoteRepository.findById(noteId).
                orElseThrow(() -> new ResourceNotFoundException("Note not found with this :" + noteId));
        favouriteNoteRepository.delete(favouriteNote);

    }
    @Override
    public List<FavouriteNoteDto> getUserFavoriteNotes() throws Exception {
        int userId=1;
        List<FavouriteNote> favouriteNotes = favouriteNoteRepository.findByUserId(userId);
        return favouriteNotes.stream().map(note -> modelMapper.map(note,FavouriteNoteDto.class)).toList();
    }

    @Override
    public boolean copyNotes(Integer noteId ) throws Exception {
        Notes notes = notesRepository.findById(noteId).
                orElseThrow(() -> new ResourceNotFoundException("Note not found with this :" + noteId));

        Notes copyNote = Notes.builder()
                .title(notes.getTitle())
                .description(notes.getDescription())
                .category(notes.getCategory())
                .isDeleted(false)
                .fileDetails(null)
                .build();
        Notes saveCopyNotes=notesRepository.save(copyNote);
        if (!ObjectUtils.isEmpty(saveCopyNotes)){
            return true;
        }
        return false;
    }

}
