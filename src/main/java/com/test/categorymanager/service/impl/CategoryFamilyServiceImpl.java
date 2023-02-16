package com.test.categorymanager.service.impl;

import com.test.categorymanager.model.Category;
import com.test.categorymanager.repository.CategoryRepository;
import com.test.categorymanager.service.CategoryFamilyService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryFamilyServiceImpl implements CategoryFamilyService {

    private final CategoryRepository categoryRepository;

    public CategoryFamilyServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Optional<Category> getParent(Category category) {
        return categoryRepository.findByName(category.getParentName());
    }

    @Override
    public List<Category> getChildren(Category category) {
        return new ArrayList<>();
    }

    @Override
    public List<Category> getAncestors(Category category) {
        return new ArrayList<>();
    }
}
