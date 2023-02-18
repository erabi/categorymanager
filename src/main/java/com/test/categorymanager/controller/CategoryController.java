package com.test.categorymanager.controller;

import com.test.categorymanager.aspect.exception.CategoryHasChildrenException;
import com.test.categorymanager.aspect.exception.CategoryNotExistsException;
import com.test.categorymanager.aspect.exception.IllegalCategoryNameFormatException;
import com.test.categorymanager.dto.CategoryDTO;
import com.test.categorymanager.dto.CategoryWithFamilyDTO;
import com.test.categorymanager.dto.mapper.CategoryMapper;
import com.test.categorymanager.dto.mapper.CategoryWithFamilyMapper;
import com.test.categorymanager.model.Category;
import com.test.categorymanager.service.CategoryConsultationService;
import com.test.categorymanager.service.CategoryManipulationService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private static final Integer NUMBER_OF_ELEMENTS_PER_PAGE = 20;
    private final CategoryConsultationService categoryConsultationService;
    private final CategoryManipulationService categoryManipulationService;

    private final CategoryMapper categoryMapper;
    private final CategoryWithFamilyMapper categoryWithFamilyMapper;

    public CategoryController(CategoryConsultationService categoryConsultationService, CategoryManipulationService categoryManipulationService, CategoryMapper categoryMapper, CategoryWithFamilyMapper categoryWithFamilyMapper) {
        this.categoryConsultationService = categoryConsultationService;
        this.categoryManipulationService = categoryManipulationService;
        this.categoryMapper = categoryMapper;
        this.categoryWithFamilyMapper = categoryWithFamilyMapper;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<CategoryDTO>> getCategories(@RequestParam(value = "page", defaultValue = "0") Integer page) {
        Page<Category> categoryPage = categoryConsultationService.getCategories((page >= 0) ? page : 0, NUMBER_OF_ELEMENTS_PER_PAGE);
        Page<CategoryDTO> categoryDTOPage = categoryPage.map(categoryMapper::toDTO);

        return new ResponseEntity<>(categoryDTOPage, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryWithFamilyDTO> getCategoryById(@PathVariable @NonNull Long id) {
        Optional<Category> category = categoryConsultationService.getById(id);
        if (category.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category doesn't exist in database", new CategoryNotExistsException(id));
        }

        return new ResponseEntity<>(category.map(categoryWithFamilyMapper::toDTO).get(), HttpStatus.OK);
    }

    //TODO : get category by name like
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody @NonNull Category category) {
        if (categoryConsultationService.getByName(category.getName()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Category [" + category.getName() + "] already exists");
        }

        return new ResponseEntity<>(categoryMapper.toDTO(categoryManipulationService.save(category)), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateCategoryName(@PathVariable Long id, @RequestBody @NonNull CategoryDTO categoryDTO) {
        if (!id.equals(categoryDTO.getId())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (categoryConsultationService.getById(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category doesn't exist in database", new CategoryNotExistsException(id));
        }
        if (categoryConsultationService.getByName(categoryDTO.getName()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Category name [" + categoryDTO.getName() + "] already exists");
        }
        try {
            categoryManipulationService.cascadeUpdateName(categoryMapper.fromDTO(categoryDTO));
            // On pourrait retourner le nombre d'enfants ayant été modifiés en cascade
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalCategoryNameFormatException ex) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage(), ex);
        } catch (CategoryNotExistsException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChildlessCategory(@PathVariable @NonNull Long id) {
        categoryConsultationService.getById(id).ifPresentOrElse(c -> {
            try {
                categoryManipulationService.deleteChildlessCategory(c);
            } catch (CategoryHasChildrenException ex) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage(), ex);
            }
        }, () -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category doesn't exist in database", new CategoryNotExistsException(id));
        });

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
