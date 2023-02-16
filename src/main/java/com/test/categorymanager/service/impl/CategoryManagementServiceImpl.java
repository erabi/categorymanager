package com.test.categorymanager.service.impl;

import com.test.categorymanager.aspect.exception.CategoryNotExistsException;
import com.test.categorymanager.aspect.exception.IllegalCategoryNameFormatException;
import com.test.categorymanager.model.Category;
import com.test.categorymanager.repository.CategoryRepository;
import com.test.categorymanager.service.CategoryManagementService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryManagementServiceImpl implements CategoryManagementService {

    private final CategoryRepository categoryRepository;

    public CategoryManagementServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category save(Category category) {
        return this.categoryRepository.save(category);
    }

    @Override
    public void cascadeUpdateName(Category category) throws IllegalCategoryNameFormatException, CategoryNotExistsException {
        if (!Category.isValidName(category.getName())) {
            throw new IllegalCategoryNameFormatException(category.getName());
        }

        Category oldCategory = categoryRepository.findById(category.getId())
                .orElseThrow(() -> new CategoryNotExistsException(category.getId()));

        List<Category> categories = categoryRepository.findByNameStartsWith(oldCategory.getName() + ".");
        categories.forEach(cat -> cat.setName(cat.getName().replace(oldCategory.getName() + ".", category.getName() + ".")));
        categories.add(category);
        categoryRepository.saveAll(categories);
    }
}
