package com.test.categorymanager.controller;

import com.test.categorymanager.CategorymanagerApplicationTests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SqlGroup({@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {"classpath:datasets/integration/before/category_test_before.sql"}, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED)), @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = {"classpath:datasets/integration/after/category_test_after.sql"}, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))})
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class CategoryControllerIntegrationTest extends CategorymanagerApplicationTests {

    private static final String CATEGORIES_ENDPOINT = "/api/categories";
    private static final String BASIC_AUTH = "Basic dXNlcjE6dXNlcjFQd2Q=";
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
    public void getCategories_withNoPageParam_shouldReturnCategoriesDefaultPage0_and_HttpStatus200() throws Exception {
        mockMvc.perform(addBasicAuth(get(CATEGORIES_ENDPOINT)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("size").value(20))
                .andExpect(jsonPath("number").value(0))
                .andExpect(jsonPath("$.content", hasSize(20)))
                .andExpect(jsonPath("$.content.[0].name").value("category.1"))
                .andExpect(jsonPath("$.content.[19].name").value("category.1.1.2"));
    }

    @Test
    public void getCategories_withNegativePageParam_shouldReturnCategoriesDefaultPage0_and_HttpStatus200() throws Exception {
        mockMvc.perform(addBasicAuth(get(CATEGORIES_ENDPOINT).param("page", String.valueOf(-2))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("size").value(20))
                .andExpect(jsonPath("number").value(0))
                .andExpect(jsonPath("$.content", hasSize(20)))
                .andExpect(jsonPath("$.content.[0].name").value("category.1"))
                .andExpect(jsonPath("$.content.[19].name").value("category.1.1.2"));
    }

    @Test
    public void getCategories_withPage2_shouldReturnCategoriesPage2_and_HttpStatus200() throws Exception {
        mockMvc.perform(addBasicAuth(get(CATEGORIES_ENDPOINT).param("page", String.valueOf(2))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("size").value(20))
                .andExpect(jsonPath("$.pageable.offset").value(40))
                .andExpect(jsonPath("number").value(2))
                .andExpect(jsonPath("$.content", hasSize(20)))
                .andExpect(jsonPath("$.content.[0].name").value("category.1.6.8.7.5"))
                .andExpect(jsonPath("$.content.[19].name").value("category.10.1.1.1.2"));
    }
}

