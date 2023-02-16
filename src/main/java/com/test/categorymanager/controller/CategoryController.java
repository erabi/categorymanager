package com.test.categorymanager.controller;

import com.test.categorymanager.aspect.exception.CategoryNotExistsException;
import com.test.categorymanager.aspect.exception.IllegalCategoryNameFormatException;
import com.test.categorymanager.dto.CategoryDTO;
import com.test.categorymanager.dto.mapper.CategoryMapper;
import com.test.categorymanager.model.Category;
import com.test.categorymanager.service.CategoryConsultationService;
import com.test.categorymanager.service.CategoryManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private static final Integer NUMBER_OF_ELEMENTS_PER_PAGE = 20;
    private final CategoryConsultationService categoryConsultationService;
    private final CategoryManagementService categoryManagementService;

    private final CategoryMapper categoryMapper;

    public CategoryController(CategoryConsultationService categoryConsultationService, CategoryManagementService categoryManagementService, CategoryMapper categoryMapper) {
        this.categoryConsultationService = categoryConsultationService;
        this.categoryManagementService = categoryManagementService;
        this.categoryMapper = categoryMapper;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<CategoryDTO>> getCategories(@RequestParam(value = "page", defaultValue = "0") Integer page) {
        Page<Category> categoryPage = categoryConsultationService.getCategories((page >= 0) ? page : 0, NUMBER_OF_ELEMENTS_PER_PAGE);
        Page<CategoryDTO> categoryDTOPage = categoryPage.map(categoryMapper::toDTO);

        return new ResponseEntity<>(categoryDTOPage, HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody @NonNull Category category) {
        if (categoryConsultationService.getByName(category.getName()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Category [" + category.getName() + "] already exists");
        }
        return new ResponseEntity<>(categoryMapper.toDTO(categoryManagementService.save(category)), HttpStatus.OK);
    }

    //TODO : edit category name
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryDTO> updateCategoryName(@PathVariable Long id, @RequestBody @NonNull CategoryDTO categoryDTO) {
        if (!id.equals(categoryDTO.getId())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (categoryConsultationService.getById(id).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (categoryConsultationService.getByName(categoryDTO.getName()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Category name [" + categoryDTO.getName() + "] alreqdy exists");
        }
        try {
            categoryManagementService.cascadeUpdateName(categoryMapper.fromDTO(categoryDTO));
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalCategoryNameFormatException | CategoryNotExistsException ex) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage(), ex);
        }
    }

    //TODO : delete childless category

    //TODO : get category

    //TODO : get category by name like
}
