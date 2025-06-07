package org.anand.mynoteapp.controller;

import org.anand.mynoteapp.dto.FavouriteNoteDto;
import org.anand.mynoteapp.dto.NotesDto;
import org.anand.mynoteapp.dto.NotesResponse;
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

    @GetMapping("/user-notes")
    public ResponseEntity<?> getAllNotesByUser(@RequestParam(name = "pageNo",defaultValue = "0")
                                               Integer pageNo, @RequestParam(name = "pageSize",defaultValue = "10") Integer pageSize){
         Integer userId=1;
         NotesResponse notes = notesService.getAllNotesByUser(userId,pageNo,pageSize);
         return CommonUtil.createBuildResponse(notes, HttpStatus.OK);
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<?> deleteNotes(@PathVariable Integer id) throws Exception {
        notesService.softDeleteNotes(id);
        return CommonUtil.createBuildResponse("Note deleted successfully", HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> hardDeleteNotes(@PathVariable Integer id) throws Exception {
        notesService.hardDeleteNotes(id);
        return CommonUtil.createBuildResponse("Note deleted successfully", HttpStatus.OK);
    }

    @GetMapping("/restore/{id}")
    public ResponseEntity<?> restoreNotes(@PathVariable Integer id) throws Exception {
        notesService.restoreNotes(id);
        return CommonUtil.createBuildResponse("Note restored successfully", HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> emptyRecyleBin() throws Exception {
        int userId=1;
        notesService.emptyRecycleBin(userId);
        return CommonUtil.createBuildResponse("Recycle Bin empty successfully", HttpStatus.OK);
    }

    @GetMapping("/recycle-bin")
    public ResponseEntity<?> getUserRecycleBinNotes() throws Exception {
        Integer userId = 2;
        List<NotesDto> notes = notesService.getUserRecycleBinNotes(userId);
        if (CollectionUtils.isEmpty(notes)) {
            return CommonUtil.createBuildResponse("Notes not avaible in Recycle Bin", HttpStatus.OK);
        }
        return CommonUtil.createBuildResponse(notes, HttpStatus.OK);
    }

    @GetMapping("/fav/{noteId}")
    public ResponseEntity<?> favoriteNote(@PathVariable Integer noteId) throws Exception {
        notesService.favoriteNotes(noteId);
        return CommonUtil.createBuildResponse("Notes added Favorite", HttpStatus.CREATED);
    }

    @DeleteMapping("/un-fav/{favNotId}")
    public ResponseEntity<?> unFavoriteNote(@PathVariable Integer favNotId) throws Exception {
        notesService.unFavoriteNotes(favNotId);
        return CommonUtil.createBuildResponse("Remove Favorite", HttpStatus.OK);
    }

    @GetMapping("/fav-note")
    public ResponseEntity<?> getUserfavoriteNote() throws Exception {

        List<FavouriteNoteDto> userFavoriteNotes = notesService.getUserFavoriteNotes();
        if (CollectionUtils.isEmpty(userFavoriteNotes)) {
            return ResponseEntity.noContent().build();
        }
        return CommonUtil.createBuildResponse(userFavoriteNotes, HttpStatus.OK);
    }
}
