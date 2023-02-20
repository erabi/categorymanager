package com.test.categorymanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.categorymanager.CategorymanagerApplicationTests;
import com.test.categorymanager.model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.com.google.common.net.HttpHeaders;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SqlGroup({@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {"classpath:datasets/integration/before/category_test_before.sql"}, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED)), @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = {"classpath:datasets/integration/after/category_test_after.sql"}, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))})
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class CategoryControllerIntegrationTest extends CategorymanagerApplicationTests {

    private static final String CATEGORIES_ENDPOINT = "/api/categories";
    private static final String BASIC_AUTH = "Basic dXNlcjE6dXNlcjFQd2Q=";
    private static final Category categoryNameExists1 = new Category("category.1");
    private static final Category categoryNameExists2 = new Category(0L, "category.1");
    private static final Category categoryIdExists = new Category(0L, "category.1000");
    private static final Category categoryNameNotExists1 = new Category("category.2000");
    private static final Category categoryNameNotExists2 = new Category(0L, "category.2000");
    private static final Category categoryIdNotExists = new Category(-1L, "category.3000");
    private static final Category categoryWrongNameFormat1 = new Category("foo");
    private static final Category categoryWrongNameFormat2 = new Category(0L, "bar");
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void init() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }


    private MockHttpServletRequestBuilder addBasicAuth(MockHttpServletRequestBuilder builder) {
        return builder.header(HttpHeaders.AUTHORIZATION, BASIC_AUTH);
    }


    @Test
    public void getCategories_withNoPageParam_shouldReturnCategoriesDefaultPage0_and_HttpStatus200OK() throws Exception {
        mockMvc.perform(addBasicAuth(get(CATEGORIES_ENDPOINT)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("size").value(20))
                .andExpect(jsonPath("number").value(0))
                .andExpect(jsonPath("$.content", hasSize(20)))
                .andExpect(jsonPath("$.content.[0].name").value("category.1"))
                .andExpect(jsonPath("$.content.[19].name").value("category.1.1.2"));
    }

    @Test
    public void getCategories_withNegativePageParam_shouldReturnCategoriesDefaultPage0_and_HttpStatus200OK() throws Exception {
        mockMvc.perform(addBasicAuth(get(CATEGORIES_ENDPOINT).param("page", String.valueOf(-2))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("size").value(20))
                .andExpect(jsonPath("number").value(0))
                .andExpect(jsonPath("$.content", hasSize(20)))
                .andExpect(jsonPath("$.content.[0].name").value("category.1"))
                .andExpect(jsonPath("$.content.[19].name").value("category.1.1.2"));
    }

    @Test
    public void getCategories_withPage2_shouldReturnCategoriesPage2_and_HttpStatus200OK() throws Exception {
        mockMvc.perform(addBasicAuth(get(CATEGORIES_ENDPOINT).param("page", String.valueOf(2))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("size").value(20))
                .andExpect(jsonPath("$.pageable.offset").value(40))
                .andExpect(jsonPath("number").value(2))
                .andExpect(jsonPath("$.content", hasSize(20)))
                .andExpect(jsonPath("$.content.[0].name").value("category.1.6.8.7.5"))
                .andExpect(jsonPath("$.content.[19].name").value("category.10.1.1.1.2"));
    }

    @Test
    public void getCategories_withNoPageParam_shouldReturnCategoriesPage0_and_HttpStatus200OK() throws Exception {
        mockMvc.perform(addBasicAuth(get(CATEGORIES_ENDPOINT)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("size").value(20))
                .andExpect(jsonPath("$.pageable.offset").value(0))
                .andExpect(jsonPath("number").value(0))
                .andExpect(jsonPath("$.content", hasSize(20)))
                .andExpect(jsonPath("$.content.[0].name").value("category.1"))
                .andExpect(jsonPath("$.content.[19].name").value("category.1.1.2"));
    }

    @Test
    public void getCategories_withNoPageParamAndNameParam_shouldReturnCategoriesNameStartsWith_and_HttpStatus200OK() throws Exception {
        mockMvc.perform(addBasicAuth(get(CATEGORIES_ENDPOINT)).param("name", "category.10.1.1.1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pageable.offset").value(0))
                .andExpect(jsonPath("number").value(0))
                .andExpect(jsonPath("$.content", hasSize(4)));
    }

    @Test
    public void getCategories_withNoPageParamAndUnknownNameParam_shouldReturnEmptyList_and_HttpStatus200OK() throws Exception {
        mockMvc.perform(addBasicAuth(get(CATEGORIES_ENDPOINT)).param("name", "foo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pageable.offset").value(0))
                .andExpect(jsonPath("number").value(0))
                .andExpect(jsonPath("$.content", hasSize(0)));
    }

    @Test
    public void getCategoryById_withIdNotExists_shouldReturnHttpStatus404NOT_FOUND() throws Exception {
        mockMvc.perform(addBasicAuth(get(CATEGORIES_ENDPOINT + "/-1")))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("404 NOT_FOUND \"Category doesn't exist in database\""))
                .andExpect(jsonPath("$.exceptionMessage").value("com.test.categorymanager.exception.CategoryNotExistsException: No category found in database for id: [-1]."));

    }

    @Test
    public void getCategoryById_withIdExists_shouldReturnCategory_and_HttpStatus200OK() throws Exception {
        mockMvc.perform(addBasicAuth(get(CATEGORIES_ENDPOINT + "/0")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value("0"))
                .andExpect(jsonPath("name").value("category.1"));

    }

    @Test
    public void createCategory_withNameNotExists_shouldReturnCategory_and_HttpStatus200OK() throws Exception {
        mockMvc.perform(addBasicAuth(post(CATEGORIES_ENDPOINT))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(categoryNameNotExists1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(61))
                .andExpect(jsonPath("name").value(categoryNameNotExists1.getName()));
    }

    @Test
    public void createCategory_withNameExists_shouldReturnHttpStatus422UNPROCESSABLE_ENTITY() throws Exception {
        mockMvc.perform(addBasicAuth(post(CATEGORIES_ENDPOINT))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(categoryNameExists1))).andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value("422 UNPROCESSABLE_ENTITY \"Category [category.1] already exists\""));
    }

    @Test
    public void createCategory_withId_shouldReturnHttpStatus422UNPROCESSABLE_ENTITY() throws Exception {
        mockMvc.perform(addBasicAuth(post(CATEGORIES_ENDPOINT))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(categoryIdExists)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value("422 UNPROCESSABLE_ENTITY \"Category must not have id\""));
    }

    @Test
    public void createCategory_withWrongNameFormat_shouldReturn_HttpStatus422UNPROCESSABLE_ENTITY() throws Exception {
        mockMvc.perform(addBasicAuth(post(CATEGORIES_ENDPOINT))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(categoryWrongNameFormat1))).andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value("422 UNPROCESSABLE_ENTITY \"Category name [foo] does not match naming requierements.\""))
                .andExpect(jsonPath("$.exceptionMessage").value("com.test.categorymanager.exception.IllegalCategoryNameFormatException: Category name [foo] does not match naming requierements."));
    }

    @Test
    public void createCategory_withIdPathVariable_shouldReturn_HttpStatus405METHOD_NOT_ALLOWED() throws Exception {
        mockMvc.perform(addBasicAuth(post(CATEGORIES_ENDPOINT + "/-1"))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(categoryNameNotExists1)))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    public void updateCategoryNameCategory_withNameNotExists_shouldReturn_HttpStatus204NO_CONTENT() throws Exception {
        mockMvc.perform(addBasicAuth(put(CATEGORIES_ENDPOINT + "/0"))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(categoryNameNotExists2)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void updateCategoryNameCategory_withIdNotExists_shouldReturn_HttpStatus404NOT_FOUND() throws Exception {
        mockMvc.perform(addBasicAuth(put(CATEGORIES_ENDPOINT + "/-1"))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(categoryIdNotExists)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("404 NOT_FOUND \"Category doesn't exist in database\""));
    }

    @Test
    public void updateCategoryName_withPathIdDifferentFromBodyId_shouldReturnCategory_and_HttpStatus400BAD_REQUEST() throws Exception {
        mockMvc.perform(addBasicAuth(put(CATEGORIES_ENDPOINT + "/2"))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(categoryIdExists)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateCategoryName_withNameExists_shouldReturnHttpStatus422UNPROCESSABLE_ENTITY() throws Exception {
        mockMvc.perform(addBasicAuth(put(CATEGORIES_ENDPOINT + "/0"))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(categoryNameExists2)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value("422 UNPROCESSABLE_ENTITY \"Category name [category.1] already exists\""));
    }

    @Test
    public void updateCategoryName_withWrongNameFormat_shouldReturn_HttpStatus422UNPROCESSABLE_ENTITY() throws Exception {
        mockMvc.perform(addBasicAuth(put(CATEGORIES_ENDPOINT + "/0"))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(categoryWrongNameFormat2)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value("422 UNPROCESSABLE_ENTITY \"Category name [bar] does not match naming requierements.\""))
                .andExpect(jsonPath("$.exceptionMessage").value("com.test.categorymanager.exception.IllegalCategoryNameFormatException: Category name [bar] does not match naming requierements."));
    }

    @Test
    public void updateCategoryName_withIdPathVariableNotExists_shouldReturn_HttpStatus405METHOD_NOT_ALLOWED() throws Exception {
        mockMvc.perform(addBasicAuth(put(CATEGORIES_ENDPOINT))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(categoryNameNotExists2)))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    public void deleteChildlessCategory_withIdPathVariableNotExists_shouldReturn_HttpStatus405METHOD_NOT_ALLOWED() throws Exception {
        mockMvc.perform(addBasicAuth(delete(CATEGORIES_ENDPOINT)))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    public void deleteChildlessCategory_withIdNotExists_shouldReturn_HttpStatus404NOT_FOUND() throws Exception {
        mockMvc.perform(addBasicAuth(delete(CATEGORIES_ENDPOINT + "/-1")))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("404 NOT_FOUND \"Category doesn't exist in database\""));
    }

    @Test
    public void deleteChildlessCategory_withCategoryHasChildren_shouldReturn_HttpStatus422UNPROCESSABLE_ENTITY() throws Exception {
        mockMvc.perform(addBasicAuth(delete(CATEGORIES_ENDPOINT + "/54")))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value("422 UNPROCESSABLE_ENTITY \"Cannot delete category with children. Found 1: category.10.1].\""));
    }

    @Test
    public void deleteChildlessCategory_withChildlessCategory_shouldReturn_HttpStatus204NO_CONTENT() throws Exception {
        mockMvc.perform(addBasicAuth(delete(CATEGORIES_ENDPOINT + "/60")))
                .andExpect(status().isNoContent());
    }
}

