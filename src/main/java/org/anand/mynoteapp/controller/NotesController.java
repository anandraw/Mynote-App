package org.anand.mynoteapp.controller;

import org.anand.mynoteapp.dto.NotesDto;
import org.anand.mynoteapp.entity.FileDetails;
import org.anand.mynoteapp.service.NotesService;
import org.anand.mynoteapp.utils.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notes")
public class NotesController{
    
    @Autowired
    private NotesService notesService;

    @PostMapping("/")
    public ResponseEntity<?> saveNote(@RequestParam("notes") String notesJson,
                                      @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            boolean saved = notesService.saveNotes(notesJson, file);
            if (saved) {
                return CommonUtil.createBuildResponse("Note saved successfully", HttpStatus.CREATED);
            }
            return CommonUtil.createErrorResponseMessage("Note not saved", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception ex) {
            return CommonUtil.createErrorResponseMessage("Error: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/")
    public ResponseEntity<?> getAllNotes() {
        List<NotesDto> notes = notesService.getAllNotes();
        if (CollectionUtils.isEmpty(notes)) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        return CommonUtil.createBuildResponse(notes, HttpStatus.OK);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<?> downloadFile(@PathVariable Integer id) throws Exception {
        FileDetails fileDetails = notesService.getFileDetails(id);
        byte[] data = notesService.downloadFile(fileDetails);

        HttpHeaders headers = new HttpHeaders();
        String contentType = CommonUtil.getContentType(fileDetails.getOriginalFileName());
        headers.setContentType(MediaType.parseMediaType(contentType));
        headers.setContentDispositionFormData("attachment", fileDetails.getOriginalFileName());

        return ResponseEntity.ok().headers(headers).body(data);
    }

}
