package com.test.categorymanager.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CategoryTest {

    @Test
    public void fromList_withListNotNull_shouldReturnCategoryWithName() {
        List<Integer> categoryAsList = Arrays.asList(1, 2, 3, 4);
        Category expectedCategory = new Category("category.1.2.3.4");

        assertEquals(expectedCategory, Category.fromList(categoryAsList));
    }

    @Test
    public void fromList_withNullList_shouldThrowIllegalArgumentException() {
        String expectedMessage = "list cannot be null";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> Category.fromList(null));
        assertEquals(expectedMessage, exception.getMessage());
    }
}
