package com.test.categorymanager.service.impl;

import com.test.categorymanager.model.Category;
import com.test.categorymanager.repository.CategoryRepository;
import com.test.categorymanager.service.CategoryFamilyService;
import org.apache.commons.lang3.StringUtils;
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
        if (category == null) {
            return new ArrayList<>();
        }

        String name = category.getName();
        long level = 1;
        String regexChildren = "^(" + name + ")(\\.([1-9]|10)){" + level + "}$";

        return categoryRepository.findChildren(regexChildren);
    }

    @Override
    public List<Category> getAncestors(Category category) {
        if (category == null) {
            return new ArrayList<>();
        }

        String name = category.getName();
        List<String> ancestors = new ArrayList<>();
        long level = getLevel(name);

        for (long i = level; i > 1; i--) {
            name = name.substring(0, name.lastIndexOf("."));
            ancestors.add(name);
        }

        return categoryRepository.findByNameIn(ancestors);
    }

    private Long getLevel(String name) {
        return StringUtils.isBlank(name) ? 0 :
                name.chars()
                        .filter(c -> c == '.')
                        .count();
    }
}
