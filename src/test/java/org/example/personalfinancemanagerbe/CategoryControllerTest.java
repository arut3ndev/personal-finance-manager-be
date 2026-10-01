package org.example.personalfinancemanagerbe;

import org.example.personalfinancemanagerbe.controllers.CategoryController;
import org.example.personalfinancemanagerbe.exceptions.NotFoundException;
import org.example.personalfinancemanagerbe.models.CategoryModel;
import org.example.personalfinancemanagerbe.services.CategoryService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoryService categoryService;

    private CategoryModel testCategory(){
        return new CategoryModel(
                1L,
                "Food",
                "Food category",
                null
        );
    }

    @Test
    public void testGetAllCategories() throws Exception {
        when(categoryService.getAllCategories()).thenReturn(List.of(
                new CategoryModel(1L, "Food", "Food category", null),
                new CategoryModel(2L, "Entertainment", "Entertainment category", null)));

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Food")))
                .andExpect(jsonPath("$[1].name", is("Entertainment")));
    }

    @Test
    public void testGetCategoryById_ReturnsOk() throws Exception{
        when(categoryService.getCategoryById(1L)).thenReturn(testCategory());
        mockMvc.perform(get("/api/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Food")));
    }

    @Test
    public void testGetCategoryById_ReturnsNotFound() throws Exception{
        when(categoryService.getCategoryById(1L)).thenThrow(new NotFoundException("Category", 1L));

        mockMvc.perform(get("/api/categories/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Not found Category with id: 1")));
    }

    @Test
    public void testPostCategory_ReturnsOk() throws Exception{
        when(categoryService.saveCategory(any())).thenReturn(testCategory());
        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "name": "Food",
                              "description": "Groceries"
                            }
                            """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", endsWith("/api/categories/1")));
    }

    @Test
    public void testPostCategory_postBlankName_Returns400() throws Exception {
        when(categoryService.saveCategory(any())).thenReturn(testCategory());
        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "name": "",
                              "description": "Groceries"
                            }
                            """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldViolationList[0].field", is("name")));
    }

    @Test
    public void testPostCategory_postMalformedBodyOnName_Returns400() throws Exception {
        when(categoryService.saveCategory(any())).thenReturn(testCategory());
        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "name": Food,
                              "description": "Groceries"
                            }
                            """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Malformed request body")));
    }

    @Test
    public void testPutCategory_ReturnOk() throws Exception{
        when(categoryService.updateCategory(any(), eq(1L))).thenReturn(testCategory());

        mockMvc.perform(put("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                      "name": "Food",
                      "description": "Groceries"
                    }
                    """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Food")));
    }

    @Test
    public void testDeleteCategory_ReturnNoContent() throws Exception{
        mockMvc.perform(delete("/api/categories/1"))
                .andExpect(status().isNoContent());
    }
}