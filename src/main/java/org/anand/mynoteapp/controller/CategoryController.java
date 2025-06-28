package org.anand.mynoteapp.controller;

import org.anand.mynoteapp.dto.CategoryDto;
import org.anand.mynoteapp.dto.CategoryResponse;
import org.anand.mynoteapp.endpoint.CategoryEndpoint;
import org.anand.mynoteapp.service.CategoryService;
import org.anand.mynoteapp.utils.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class CategoryController implements CategoryEndpoint {

    @Autowired
    private CategoryService categoryService;

    @Override
    public ResponseEntity<?> saveCategory(CategoryDto category) {
        Boolean savedCategory = categoryService.saveCategory(category);
        if (savedCategory) {
            return CommonUtil.createBuildResponseMessage("saved success", savedCategory,HttpStatus.CREATED);
        }else {
            return CommonUtil.createErrorResponseMessage("save failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> getAllCategory() {
        List<CategoryDto> categoryList = categoryService.getAllCategory();
        if (CollectionUtils.isEmpty(categoryList)) {
            return ResponseEntity.noContent().build();
        }else{
            return CommonUtil.createBuildResponse(categoryList, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<?> getCategoryById(Integer id) throws Exception {
        CategoryDto categoryById = categoryService.getCategoryById(id);
        if (ObjectUtils.isEmpty(categoryById)) {
            return CommonUtil.createErrorResponseMessage("category not found", HttpStatus.NOT_FOUND);
        }
        else {
            return CommonUtil.createBuildResponse(categoryById, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<?> getActiveCategory() {
        List<CategoryResponse> allCategory=categoryService.getActiveCategory();
        if (CollectionUtils.isEmpty(allCategory)) {
            return ResponseEntity.noContent().build();
        }
        else{
            return CommonUtil.createBuildResponse(allCategory, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<?> deleteCategoryById(Integer id){
        Boolean deleted = categoryService.deleteCategory(id);
        if (deleted){
            return CommonUtil.createBuildResponse("category deleted success", HttpStatus.OK);
        }
        else{
            return CommonUtil.createErrorResponseMessage("category not deleted", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
