package com.test.categorymanager.controller;

import com.test.categorymanager.model.Category;
import com.test.categorymanager.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private static final Integer NUMBER_OF_ELEMENTS_PER_PAGE = 20;
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<Category>> getCategories(@RequestParam(value = "page", defaultValue = "0") Integer page) {
        return new ResponseEntity<>(categoryService.getCategories((page >= 0) ? page : 0, NUMBER_OF_ELEMENTS_PER_PAGE), HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        if (category == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (categoryService.getCategoryByName(category.getName()) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Category [" + category.getName() + "] already exists");
        }

        return new ResponseEntity<>(categoryService.addCategory(category), HttpStatus.OK);
    }

    //TODO : edit category name

    //TODO : delete childless category

    //TODO : get category

    //TODO : get category by name like
}
