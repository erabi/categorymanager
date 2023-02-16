package com.test.categorymanager.model;

import com.test.categorymanager.aspect.exception.IllegalCategoryNameFormatException;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    public void isValidName_withFirstLevelValidName_returnsTrue() {
        String validName = "category.1";
        assertTrue(Category.isValidName(validName));
    }

    @Test
    public void isValidName_withChildLevelValidName_returnsTrue() {
        String validName = "category.1.1.2.3.4.5.6.7.8.9.10";
        assertTrue(Category.isValidName(validName));
    }

    @Test
    public void isValidName_withInvalidName_returnsFalse() {
        String firstLevelZero = "category.0";
        String childLevelZero = "category.5.0";
        String moreThanTenLevels = "category.1.1.2.3.4.5.6.7.8.9.10.11.12";
        String moreThanTenChildren = "category.5.20.1";
        String endsWithPoint = "category.5.20.1.";
        String misc1 = "foo";
        String misc2 = "category.foo";
        String misc3 = "category.1.foo";

        assertFalse(Category.isValidName(firstLevelZero));
        assertFalse(Category.isValidName(childLevelZero));
        assertFalse(Category.isValidName(moreThanTenLevels));
        assertFalse(Category.isValidName(moreThanTenChildren));
        assertFalse(Category.isValidName(endsWithPoint));
        assertFalse(Category.isValidName(misc1));
        assertFalse(Category.isValidName(misc2));
        assertFalse(Category.isValidName(misc3));
    }

    @Test
    public void getParentName_withValidName_returnsParentName() {
        Category category = new Category("category.3.4.5");
        String expectedParentName = "category.3.4";

        assertEquals(expectedParentName, category.getParentName());
    }

    @Test
    public void getParentName_withIValidName_returnsEmptyString() {
        Category category = new Category("foo.3.4.5");
        String expectedName = "";

        assertEquals(expectedName, category.getParentName());
    }
}
