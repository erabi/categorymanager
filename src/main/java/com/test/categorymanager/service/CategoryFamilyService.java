package com.test.categorymanager.service;

import com.test.categorymanager.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryFamilyService {

    Optional<Category> getParent(Category category);

    List<Category> getChildren(Category category);

    List<Category> getAncestors(Category category);
}
