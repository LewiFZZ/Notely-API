package com.notely.api.notely.service.classes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.notely.api.notely.dto.CategoryDTO;
import com.notely.api.notely.entity.Category;
import com.notely.api.notely.entity.AppUser;
import com.notely.api.notely.repository.CategoryRepository;
import com.notely.api.notely.service.interfaces.CategoryServiceI;
import com.notely.api.notely.util.UserContext;

@Service
public class CategoryService implements CategoryServiceI {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        AppUser currentUser = UserContext.getCurrentUser();
        if (currentUser == null) {
            throw new SecurityException("User not authenticated");
        }

        try {
            return categoryRepository.findByUserAndIsActiveTrue(currentUser)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error obtaining categories: " + e.getMessage());
            return List.of();
        }
    }

    @Override
    public Optional<CategoryDTO> getCategoryById(Long id) {
        AppUser currentUser = UserContext.getCurrentUser();
        if (currentUser == null) {
            throw new SecurityException("User not authenticated");
        }

        try {
            return categoryRepository.findByIdAndUser(id, currentUser)
                .map(this::toDTO);
        } catch (Exception e) {
            System.err.println("Error obtaining the desired category with ID " + id + ": " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        AppUser currentUser = UserContext.getCurrentUser();
        if (currentUser == null) {
            throw new SecurityException("User not authenticated");
        }

        Category category = new Category();
        category.setName(categoryDTO.getName());
        category.setUser(currentUser);
        category.setActive(true);

        Category savedCategory = categoryRepository.save(category);
        return toDTO(savedCategory);
    }

    @Override
    @Transactional
    public Optional<CategoryDTO> updateCategory(Long id, CategoryDTO categoryDTO) {
        AppUser currentUser = UserContext.getCurrentUser();
        if (currentUser == null) {
            throw new SecurityException("User not authenticated");
        }

        Optional<Category> categoryOpt = categoryRepository.findByIdAndUser(id, currentUser);
        if (categoryOpt.isEmpty()) {
            return Optional.empty();
        }

        Category category = categoryOpt.get();
        category.setName(categoryDTO.getName());

        Category updatedCategory = categoryRepository.save(category);
        return Optional.of(toDTO(updatedCategory));
    }

    @Override
    @Transactional
    public boolean deleteCategory(Long id) {
        AppUser currentUser = UserContext.getCurrentUser();
        if (currentUser == null) {
            throw new SecurityException("User not authenticated");
        }

        Optional<Category> categoryOpt = categoryRepository.findByIdAndUser(id, currentUser);
        if (categoryOpt.isEmpty()) {
            return false;
        }

        Category category = categoryOpt.get();
        category.setActive(false); // Soft delete
        categoryRepository.save(category);
        return true;
    }

    private CategoryDTO toDTO(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        if (category.getUser() != null) {
            dto.setUserId(category.getUser().getUid());
        }
        return dto;
    }
}

