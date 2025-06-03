package org.anand.mynoteapp.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.anand.mynoteapp.dto.NotesDto;
import org.anand.mynoteapp.entity.FileDetails;
import org.anand.mynoteapp.entity.Notes;
import org.anand.mynoteapp.exception.ResourceNotFoundException;
import org.anand.mynoteapp.repository.CategoryRepository;
import org.anand.mynoteapp.repository.FileRepository;
import org.anand.mynoteapp.repository.NotesRepository;
import org.anand.mynoteapp.service.NotesService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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


    @Override
    public Boolean saveNotes(String notes, MultipartFile file) throws Exception {
        ObjectMapper ob = new ObjectMapper();
        NotesDto notesDto = ob.readValue(notes, NotesDto.class);

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

            // ✅ Ensure upload folder exists
            File uploadFolder = new File(uploadpath);
            if (!uploadFolder.exists()) {
                uploadFolder.mkdirs(); // creates the full directory structure if not present
            }

            // ✅ Define the full path to store file
            String storePath = uploadpath + File.separator + uploadFileName;

            // ✅ Save file
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
}
