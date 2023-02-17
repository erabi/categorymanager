package com.test.categorymanager.service;

import com.test.categorymanager.model.Category;
import com.test.categorymanager.repository.CategoryRepository;
import com.test.categorymanager.service.impl.CategoryManipulationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CategoryConsultationServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryManipulationServiceImpl categoryManagementService;

    @Test
    public void save_withCategory_shouldReturnCategory() {
        Category category = new Category("category.1.2.3");
        when(categoryRepository.save(category)).thenReturn(category);
        Category savedCategory = categoryManagementService.save(category);

        assertEquals(savedCategory, category);
    }
}
