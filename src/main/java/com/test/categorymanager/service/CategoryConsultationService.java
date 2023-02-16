package com.test.categorymanager.service;

import com.test.categorymanager.model.Category;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface CategoryConsultationService {
    Page<Category> getCategories(Integer page, Integer numberOfElements);

    Optional<Category> getByName(String name);

    Optional<Category> getById(Long id);
}
