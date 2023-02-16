package com.test.categorymanager.aspect.exception;

import lombok.Getter;

@Getter
public class IllegalCategoryNameFormatException extends Exception {

    private final String categoryName;
    private final String message = "Category name [" + getCategoryName() + "] does not match naming requierements.";

    public IllegalCategoryNameFormatException(String categoryName) {
        super();
        this.categoryName = categoryName;
    }
}
