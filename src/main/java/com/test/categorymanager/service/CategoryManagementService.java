package com.test.categorymanager.service;

import com.test.categorymanager.aspect.exception.CategoryNotExistsException;
import com.test.categorymanager.aspect.exception.IllegalCategoryNameFormatException;
import com.test.categorymanager.model.Category;

public interface CategoryManagementService {

    Category save(Category category);

    void cascadeUpdateName(Category category) throws IllegalCategoryNameFormatException, CategoryNotExistsException;
}
