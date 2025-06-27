package org.anand.mynoteapp.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EmailReuest {
    private String to;
    private String subject;
    private String title;
    private String message;
}
