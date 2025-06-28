package org.anand.mynoteapp.endpoint;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Tag(name = "Notes", description = "Apis for notes controller")
@RequestMapping("/api/v1/notes")
public interface NotesEndPoint {

    @Operation(
            summary = "Save a note",
            tags = "{Notes}",
            description = "Save a note with optional file attachment"
    )
    @PostMapping("/")
    public ResponseEntity<?> saveNote(@RequestParam("notes") String notesJson,
                                      @RequestParam(value = "file", required = false) MultipartFile file);

    @Operation(
            summary = "Get all notes",
            tags = "{Notes}",
            description = "Retrieve all notes of the current user"
    )
    @GetMapping("/")
    public ResponseEntity<?> getAllNotes();


    @Operation(
            summary = "Download file",
            tags = "{Notes}",
            description = "Download the attached file of a note by note ID"
    )
    @GetMapping("/download/{id}")
    public ResponseEntity<?> downloadFile(@PathVariable Integer id) throws Exception;



    @Operation(
            summary = "Paginated user notes",
            tags = {"Notes"},
            description = "Get all notes of the current user with pagination"
    )
    @GetMapping("/user-notes")
    public ResponseEntity<?> getAllNotesByUser(@RequestParam(name = "pageNo",defaultValue = "0")
                                               Integer pageNo, @RequestParam(name = "pageSize",defaultValue = "10") Integer pageSize);

    @Operation(
            summary = "Soft delete a note",
            tags = "{Notes}",
            description = "Move a note to recycle bin (soft delete)"
    )
    @GetMapping("/delete/{id}")
    public ResponseEntity<?> deleteNotes(@PathVariable Integer id) throws Exception;


    @Operation(
            summary = "Hard delete a note",
            tags = "{Notes}",
            description = "Permanently delete a note from the system"
    )
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> hardDeleteNotes(@PathVariable Integer id) throws Exception;

    @Operation(
            summary = "Restore a note",
            tags = "{Notes}",
            description = "Restore a soft-deleted note from recycle bin"
    )
    @GetMapping("/restore/{id}")
    public ResponseEntity<?> restoreNotes(@PathVariable Integer id) throws Exception;

    @Operation(
            summary = "Empty recycle bin",
            tags = "{Notes}",
            description = "Permanently delete all soft-deleted notes of user"
    )
    @DeleteMapping("/delete")
    public ResponseEntity<?> emptyUserRecyleBin() throws Exception;

    @Operation(
            summary = "Get recycle bin notes",
            tags = "{Notes}",
            description = "Retrieve all soft-deleted notes"
    )
    @GetMapping("/recycle-bin")
    public ResponseEntity<?> getUserRecycleBinNotes() throws Exception;

    @Operation(
            summary = "Mark note as favorite",
            tags = "{Notes}",
            description = "Mark a note as favorite by note ID"
    )
    @GetMapping("/fav/{noteId}")
    public ResponseEntity<?> favoriteNote(@PathVariable Integer noteId) throws Exception;

    @Operation(
            summary = "Remove favorite note",
            tags = "{Notes}",
            description = "Remove note from favorites by favorite note ID"
    )
    @DeleteMapping("/un-fav/{favNotId}")
    public ResponseEntity<?> unFavoriteNote(@PathVariable Integer favNotId) throws Exception;

    @Operation(
            summary = "Get favorite notes",
            tags = "{Notes}",
            description = "Get all favorite notes of the user"
    )
    @GetMapping("/fav-note")
    public ResponseEntity<?> getUserfavoriteNote() throws Exception;

    @Operation(
            summary = "Copy a note",
            tags = "{Notes}",
            description = "Duplicate a note by note ID"
    )
    @GetMapping("/copy/{noteId}")
    public ResponseEntity<?> copyNote(@PathVariable Integer noteId) throws Exception;
}
