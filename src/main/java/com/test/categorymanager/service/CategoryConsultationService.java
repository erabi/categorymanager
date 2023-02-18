package com.test.categorymanager.service;

import com.test.categorymanager.model.Category;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface CategoryConsultationService {
    Page<Category> getAll(Integer page, Integer numberOfElements);

    Optional<Category> getById(Long id);

    Optional<Category> getByName(String name);

    Page<Category> getByNameStartsWith(String name, Integer page, Integer numberOfElementsPerPage);
}
