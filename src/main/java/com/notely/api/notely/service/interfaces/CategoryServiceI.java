package com.notely.api.notely.service.interfaces;

import java.util.List;
import java.util.Optional;

import com.notely.api.notely.dto.CategoryDTO;

public interface CategoryServiceI {
    List<CategoryDTO> getAllCategories();
    Optional<CategoryDTO> getCategoryById(Long id);
    CategoryDTO createCategory(CategoryDTO categoryDTO);
    Optional<CategoryDTO> updateCategory(Long id, CategoryDTO categoryDTO);
    boolean deleteCategory(Long id);
}

