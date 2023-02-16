package com.test.categorymanager.aspect.exception;

import lombok.Getter;

@Getter
public class CategoryNotExistsException extends Exception {

    private final Long idCategory;
    private final String message = "No category found in database for id: [" + this.getIdCategory() + "].";

    public CategoryNotExistsException(Long id) {
        super();
        this.idCategory = id;
    }

    private String getIdCategory() {
        return String.valueOf(this.idCategory);
    }
}
