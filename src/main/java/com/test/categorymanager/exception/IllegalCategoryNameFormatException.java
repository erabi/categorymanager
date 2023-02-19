package com.test.categorymanager.exception;

import lombok.Getter;

@Getter
public class IllegalCategoryNameFormatException extends Exception {

    private final String categoryName;

    public IllegalCategoryNameFormatException(String categoryName) {
        super();
        this.categoryName = categoryName;
    }

    public String getMessage() {
        return "Category name [" + getCategoryName() + "] does not match naming requierements.";
    }
}
