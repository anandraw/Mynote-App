package org.anand.mynoteapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FavouriteNoteDto {

    private Integer id;
    private NotesDto notes;
    private Integer userId;
}
