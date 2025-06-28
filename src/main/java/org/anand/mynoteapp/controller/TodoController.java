package org.anand.mynoteapp.controller;

import org.anand.mynoteapp.dto.TodoDto;
import org.anand.mynoteapp.endpoint.TodoEndPoint;
import org.anand.mynoteapp.service.TodoService;
import org.anand.mynoteapp.utils.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class TodoController implements TodoEndPoint {

    @Autowired
    private TodoService todoService;

    @Override
    public ResponseEntity<?> saveTodo(@RequestBody TodoDto todoDto) throws Exception {
        boolean saveTodo= todoService.saveTodo(todoDto);
        if (saveTodo){
            return CommonUtil.createErrorResponseMessage("Todo Saved Sucess", HttpStatus.OK);
        }
        return CommonUtil.createErrorResponseMessage("Todo Not Saved",HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<?> findById(@PathVariable Integer id) throws Exception {
        TodoDto toById = todoService.getToById(id);
        return CommonUtil.createBuildResponse(toById, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getAllTodoByUser() throws Exception {
        List<TodoDto> todoList = todoService.getTodoByUser();
        if (CollectionUtils.isEmpty(todoList)) {
            return ResponseEntity.noContent().build();
        }
        return CommonUtil.createBuildResponse(todoList, HttpStatus.OK);
    }
}
