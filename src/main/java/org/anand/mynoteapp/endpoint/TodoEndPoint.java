package org.anand.mynoteapp.endpoint;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.anand.mynoteapp.dto.TodoDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Todo", description = "All Todo‑related operations")
@RequestMapping("/api/v1/todo")
public interface TodoEndPoint {

    @Operation(
            summary     = "Save todo",
            description = "Create or update a todo item for the logged‑in user",
            tags        = {"Todo"}
    )
    @PostMapping("/")
    ResponseEntity<?> saveTodo(@RequestBody TodoDto todoDto) throws Exception;

    @Operation(
            summary     = "Get todo by ID",
            description = "Retrieve a single todo item by its ID",
            tags        = {"Todo"}
    )
    @GetMapping("/{id}")
    ResponseEntity<?> findById(@PathVariable Integer id) throws Exception;

    @Operation(
            summary     = "List todos",
            description = "Get all todo items belonging to the current user",
            tags        = {"Todo"}
    )
    @GetMapping("/list")
    ResponseEntity<?> getAllTodoByUser() throws Exception;
}
