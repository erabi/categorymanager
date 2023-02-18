package com.test.categorymanager.dto.mapper;

import com.test.categorymanager.dto.CategoryWithFamilyDTO;
import com.test.categorymanager.model.Category;
import com.test.categorymanager.service.CategoryFamilyService;
import org.springframework.stereotype.Component;

/**
 * Custom mapper class, for mapping Category to CategoryDTO
 * On a larger scale project we could use the MapStruct dependency to facilitate the process
 */
@Component
public class CategoryWithFamilyMapper {

    protected CategoryFamilyService categoryFamilyService;

    public CategoryWithFamilyMapper(CategoryFamilyService categoryFamilyService) {
        this.categoryFamilyService = categoryFamilyService;
    }

    public Category fromDTO(CategoryWithFamilyDTO categoryWithFamilyDTO) {
        Category category = new Category();
        category.setId(categoryWithFamilyDTO.getId());
        category.setName(categoryWithFamilyDTO.getName());
        return category;
    }

    public CategoryWithFamilyDTO toDTO(Category category) {
        CategoryWithFamilyDTO categoryWithFamilyDTO = new CategoryWithFamilyDTO();
        categoryWithFamilyDTO.setId(category.getId());
        categoryWithFamilyDTO.setName(category.getName());
        categoryFamilyService.getParent(category).ifPresent(categoryWithFamilyDTO::setParent);
        categoryWithFamilyDTO.setChildren(categoryFamilyService.getChildren(category));
        categoryWithFamilyDTO.setAncestors(categoryFamilyService.getAncestors(category));

        return categoryWithFamilyDTO;
    }
}
