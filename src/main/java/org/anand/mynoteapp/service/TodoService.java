package org.anand.mynoteapp.service;

import org.anand.mynoteapp.dto.TodoDto;
import java.util.List;

public interface TodoService {

    public boolean saveTodo(TodoDto todoDto) throws Exception;

    public TodoDto getToById(Integer id) throws Exception;

    public List<TodoDto> getTodoByUser();
}
