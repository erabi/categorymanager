package com.test.categorymanager.service;

import com.test.categorymanager.exception.CategoryHasChildrenException;
import com.test.categorymanager.exception.CategoryNotExistsException;
import com.test.categorymanager.exception.IllegalCategoryNameFormatException;
import com.test.categorymanager.model.Category;

public interface CategoryManipulationService {

    Category save(Category category);

    void cascadeUpdateName(Category category) throws IllegalCategoryNameFormatException, CategoryNotExistsException;

    void deleteChildlessCategory(Category category) throws CategoryHasChildrenException;
}
