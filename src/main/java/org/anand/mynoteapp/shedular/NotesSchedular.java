package org.anand.mynoteapp.shedular;

import org.anand.mynoteapp.entity.Notes;
import org.anand.mynoteapp.repository.NotesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class NotesSchedular {

    @Autowired
    private NotesRepository notesRepo;

    @Scheduled(cron = "0 0 0 * * ?")
//	@Scheduled(cron = "* * * ? * *")
    public void deleteNotesSchdular() {
        // 20-nov -14 nov -7days
        LocalDateTime cutOffDate = LocalDateTime.now().minusDays(7);
        List<Notes> deleteNotes = notesRepo.findAllByIsDeletedAndDeletedOnBefore(true, cutOffDate);
        notesRepo.deleteAll(deleteNotes);
    }
}
