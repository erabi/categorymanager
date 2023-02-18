package com.test.categorymanager.aspect.exception;

import lombok.Getter;

@Getter
public class CategoryNotExistsException extends Exception {

    private final Long idCategory;

    public CategoryNotExistsException(Long id) {
        super();
        this.idCategory = id;
    }

    private String getIdCategory() {
        return String.valueOf(this.idCategory);
    }

    public String getMessage() {
        return "No category found in database for id: [" + this.getIdCategory() + "].";
    }
}
