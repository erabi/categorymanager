package com.test.categorymanager.service.impl;

import com.test.categorymanager.exception.CategoryHasChildrenException;
import com.test.categorymanager.exception.CategoryNotExistsException;
import com.test.categorymanager.exception.IllegalCategoryNameFormatException;
import com.test.categorymanager.model.Category;
import com.test.categorymanager.repository.CategoryRepository;
import com.test.categorymanager.service.CategoryFamilyService;
import com.test.categorymanager.service.CategoryManipulationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryManipulationServiceImpl implements CategoryManipulationService {

    private final CategoryRepository categoryRepository;
    private final CategoryFamilyService categoryFamilyService;

    public CategoryManipulationServiceImpl(CategoryRepository categoryRepository, CategoryFamilyService categoryFamilyService) {
        this.categoryRepository = categoryRepository;
        this.categoryFamilyService = categoryFamilyService;
    }

    @Override
    public Category save(Category category) {
        return this.categoryRepository.save(category);
    }

    @Override
    public void cascadeUpdateName(Category category) throws IllegalCategoryNameFormatException, CategoryNotExistsException {
        if (!Category.isValidName(category.getName())) {
            throw new IllegalCategoryNameFormatException(category.getName());
        }

        Category oldCategory = categoryRepository.findById(category.getId())
                .orElseThrow(() -> new CategoryNotExistsException(category.getId()));

        List<Category> categories = categoryRepository.findByNameStartsWith(oldCategory.getName() + ".");
        categories.forEach(cat -> cat.setName(cat.getName().replace(oldCategory.getName() + ".", category.getName() + ".")));
        categories.add(category);
        categoryRepository.saveAll(categories);
    }

    @Override
    public void deleteChildlessCategory(Category category) throws CategoryHasChildrenException {
        /*  On pourrait aussi faire le check de l'existence d'enfants via une méthide de type EXISTS
            plus performante mais qui ne nous permet pas de remonter une erreur détaillée
         */
        List<Category> children = categoryFamilyService.getChildren(category);
        if (!children.isEmpty()) {
            throw new CategoryHasChildrenException(children);
        }
        categoryRepository.delete(category);
    }
}
