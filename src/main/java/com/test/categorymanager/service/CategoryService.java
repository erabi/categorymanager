package com.test.categorymanager.service;

import com.test.categorymanager.model.Category;
import org.springframework.data.domain.Page;

public interface CategoryService {

    Category save(Category category);

    Page<Category> getCategories(Integer page, Integer numberOfElements);
}
