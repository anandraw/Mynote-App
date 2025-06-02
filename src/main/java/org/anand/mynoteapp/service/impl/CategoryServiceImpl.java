package org.anand.mynoteapp.service.impl;

import org.anand.mynoteapp.dto.CategoryDto;
import org.anand.mynoteapp.dto.CategoryResponse;
import org.anand.mynoteapp.entity.Category;
import org.anand.mynoteapp.exception.ResourceNotFoundException;
import org.anand.mynoteapp.repository.CategoryRepository;
import org.anand.mynoteapp.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Boolean saveCategory(CategoryDto categoryDto) {
       Category category =  modelMapper.map(categoryDto, Category.class);

       if(ObjectUtils.isEmpty(categoryDto.getId())){
           category.setIsDeleted(false);
           category.setCreatedBy(1);
           category.setCreatedOn(new Date());
       }else{
           updateCategory(category);
       }
       Category saveCategory = categoryRepository.save(category);
       if(ObjectUtils.isEmpty(saveCategory)){
           return false;
       }
       return true;
    }

    private void updateCategory(Category category) {
        Optional<Category> byId = categoryRepository.findById(category.getId());
        if (byId.isPresent()) {
            Category existCategory = byId.get();
            category.setUpdatedBy(existCategory.getUpdatedBy());
            category.setUpdatedOn(existCategory.getUpdatedOn());
            category.setIsDeleted(existCategory.getIsDeleted());
        }
    }

    @Override
    public List<CategoryDto> getAllCategory() {
      List<Category> categoryDtoList =  categoryRepository.findAll();
      List<CategoryDto> list = categoryDtoList.stream().map(cat -> modelMapper.map(cat, CategoryDto.class)).toList();
      return list;
    }

    @Override
    public List<CategoryResponse> getActiveCategory() {
        List<Category> categories=categoryRepository.findByIsActiveTrueAndIsDeletedFalse();
        List<CategoryResponse> list = categories.stream().map(cat -> modelMapper.map(cat, CategoryResponse.class)).toList();
        return list;

    }

    @Override
    public CategoryDto getCategoryById(Integer id) throws Exception {
        Category category = categoryRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with this id= "+ id));
        if (!ObjectUtils.isEmpty(category)) {
            category.getName().toUpperCase();
            return modelMapper.map(category, CategoryDto.class);
        }
        return null;
    }

    @Override
    public Boolean deleteCategory(Integer id) {
        Optional<Category> findByCategory = categoryRepository.findById(id);

        if (findByCategory.isPresent()){
            Category category = findByCategory.get();
            category.setIsDeleted(true);
            categoryRepository.save(category);
            return true;
        }
        return false;
    }
}
