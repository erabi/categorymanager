package com.test.categorymanager.dto;

import com.test.categorymanager.model.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {

    private Long id;

    private String name;

    private Category parent;

    private List<Category> children;

    private List<Category> ancestors;
}
