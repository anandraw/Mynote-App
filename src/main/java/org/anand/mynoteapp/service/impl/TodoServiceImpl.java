package org.anand.mynoteapp.service.impl;

import org.anand.mynoteapp.dto.TodoDto;
import org.anand.mynoteapp.entity.Todo;
import org.anand.mynoteapp.exception.ResourceNotFoundException;
import org.anand.mynoteapp.repository.TodoRepository;
import org.anand.mynoteapp.service.TodoService;
import org.anand.mynoteapp.utils.Validation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class TodoServiceImpl implements TodoService {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private Validation validation;

    @Override
    public boolean saveTodo(TodoDto todoDto) throws Exception {

        // validate todo status
        validation.todoValidation(todoDto);

        Todo todo=modelMapper.map(todoDto, Todo.class);
        todo.setStatusId(todoDto.getStatus().getId());
        Todo saveTodo=todoRepository.save(todo);
        if (!ObjectUtils.isEmpty(saveTodo)){
            return true;
        }
        return false;
    }

    @Override
    public TodoDto getToById(Integer id) throws Exception {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Todo not found ! id invalid"));
        TodoDto todoDto = modelMapper.map(todo, TodoDto.class);
        return todoDto;
    }

    @Override
    public List<TodoDto> getTodoByUser() {
        Integer userId=1;
        List<Todo> byCreatedBy = todoRepository.findByCreatedBy(userId);
        return byCreatedBy.stream().map(td -> modelMapper.map(td,TodoDto.class)).toList();
    }
}
