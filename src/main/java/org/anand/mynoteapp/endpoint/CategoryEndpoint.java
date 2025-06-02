package org.anand.mynoteapp.endpoint;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.anand.mynoteapp.dto.CategoryDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Category", description = "All the Category operation APIs")
@RequestMapping("/api/v1/category")
public interface CategoryEndpoint {

    @Operation(summary = "save category", tags = {"Category"},description = "Admin save category")
    @PostMapping("/save")
    public ResponseEntity<?> saveCategory(@RequestBody CategoryDto category);

    @Operation(summary = "get all category",tags = {"Category"},description = "Admin get all categories")
    @GetMapping("/")
    public ResponseEntity<?> getAllCategory();

    @Operation(summary = "get category by id", tags = {"Category"}, description = "Admin get category details")
    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Integer id) throws Exception;

    @Operation(summary = "delete category by id", tags = {"Category"},description = "Admin delete the category")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategoryById(@PathVariable Integer id);

    @Operation(summary = "get active category",tags = {"Category"}, description = "Admin, User get active")
    @GetMapping("/active")
    public ResponseEntity<?> getActiveCategory();
}
