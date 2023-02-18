package com.test.categorymanager.dto.mapper;

import com.test.categorymanager.dto.CategoryDTO;
import com.test.categorymanager.model.Category;
import org.springframework.stereotype.Component;

/**
 * Classe de mapper Custom pour mapper Category à CategoryDTO
 * Pour des entités plus complexes on pourra utiliser des librairies dédiées comme MapStruct
 */
@Component
public class CategoryMapper {

    public CategoryMapper() {
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

        return categoryDTO;
    }
}
