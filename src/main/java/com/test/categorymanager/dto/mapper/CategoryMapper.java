package com.test.categorymanager.dto.mapper;

import com.test.categorymanager.dto.CategoryDTO;
import com.test.categorymanager.model.Category;
import com.test.categorymanager.service.CategoryFamilyService;
import org.springframework.stereotype.Component;

/**
 * Custom mapper class, for mapping Category to CategoryDTO
 * On a larger scale project we could use the MapStruct dependency to facilitate the process
 */
@Component
public class CategoryMapper {

    protected CategoryFamilyService categoryFamilyService;

    public CategoryMapper(CategoryFamilyService categoryFamilyService) {
        this.categoryFamilyService = categoryFamilyService;
    }

    public Category fromDTO(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setId(categoryDTO.getId());
        category.setName(categoryDTO.getName());
        return category;
    }

    public CategoryDTO toDTO(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        categoryFamilyService.getParent(category).ifPresent(categoryDTO::setParent);
        categoryDTO.setChildren(categoryFamilyService.getChildren(category));
        categoryDTO.setAncestors(categoryFamilyService.getAncestors(category));

        return categoryDTO;
    }
}
