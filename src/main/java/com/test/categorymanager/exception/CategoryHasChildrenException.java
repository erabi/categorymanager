package com.test.categorymanager.exception;

import com.test.categorymanager.model.Category;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CategoryHasChildrenException extends Exception {

    private final List<Category> children;

    public CategoryHasChildrenException(List<Category> children) {
        super();
        this.children = children;
    }

    public String getMessage() {
        return "Cannot delete category with children. " +
                "Found " + this.getChildren().size() + ": " +
                this.getChildren().stream().map(Category::getName).collect(Collectors.joining(",")) + "].";
    }
}
