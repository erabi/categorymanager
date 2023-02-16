package com.test.categorymanager.service.impl;

import com.test.categorymanager.model.Category;
import com.test.categorymanager.repository.CategoryRepository;
import com.test.categorymanager.service.CategoryConsultationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryConsultationServiceImpl implements CategoryConsultationService {

    private final CategoryRepository categoryRepository;

    public CategoryConsultationServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Page<Category> getCategories(Integer page, Integer numberOfElements) {
        Pageable categoryPageable = PageRequest.of((page != null) ? page : 0, numberOfElements);
        return categoryRepository.findAllOrderByName(categoryPageable);
    }

    @Override
    public Optional<Category> getByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public Optional<Category> getById(Long id) {
        return categoryRepository.findById(id);
    }
}
