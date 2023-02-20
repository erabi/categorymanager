package com.test.categorymanager.service;

import com.test.categorymanager.exception.CategoryNotExistsException;
import com.test.categorymanager.exception.IllegalCategoryNameFormatException;
import com.test.categorymanager.model.Category;
import com.test.categorymanager.repository.CategoryRepository;
import com.test.categorymanager.service.impl.CategoryManipulationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CategoryManipulationServiceImplTest {

    @Captor
    ArgumentCaptor<List<Category>> modifiedNames;
    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private CategoryManipulationServiceImpl categoryManipulationService;

    @Test
    public void cascadeUpdateName_withCategory_shouldUpdateDescendants() throws IllegalCategoryNameFormatException, CategoryNotExistsException {
        Category category = new Category(0L, "category.2.2.3");
        Category oldCategory = new Category(0L, "category.1.2.3");
        List<Category> descendants = new ArrayList<>();
        descendants.add(new Category("category.1.2.3.4"));
        descendants.add(new Category("category.1.2.3.5.6"));
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(oldCategory));
        when(categoryRepository.findByNameStartsWith(oldCategory.getName() + ".")).thenReturn(descendants);

        categoryManipulationService.cascadeUpdateName(category);

        verify(categoryRepository).saveAll(modifiedNames.capture());
        List<Category> updatedCategories = modifiedNames.getValue();

        assertEquals(3, updatedCategories.size());
        assertTrue(updatedCategories.contains(category));
        assertTrue(updatedCategories.contains(new Category("category.2.2.3.4")));
        assertTrue(updatedCategories.contains(new Category("category.2.2.3.5.6")));
    }

    @Test
    public void cascadeUpdateName_withInvalidNameFormat_shouldThrow_IllegalCategoryNameFormatException() throws IllegalCategoryNameFormatException, CategoryNotExistsException {
    }
}
