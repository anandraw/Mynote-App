package org.anand.mynoteapp.utils;

import org.anand.mynoteapp.dto.TodoDto;
import org.anand.mynoteapp.enums.TodoStatus;
import org.anand.mynoteapp.exception.ResourceNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class Validation {

    public void todoValidation(TodoDto todo) throws Exception {

        TodoDto.StatusDto reqStatus = todo.getStatus();
        Boolean statusFound = false;
        for (TodoStatus st : TodoStatus.values()) {
            if (st.getId().equals(reqStatus.getId())) {
                statusFound = true;
            }
        }
        if (!statusFound) {
            throw new ResourceNotFoundException("invalid status");
        }

    }
}
