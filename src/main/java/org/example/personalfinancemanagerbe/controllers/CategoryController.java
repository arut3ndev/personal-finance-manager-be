package org.example.personalfinancemanagerbe.controllers;

import org.example.personalfinancemanagerbe.models.CategoryModel;
import org.example.personalfinancemanagerbe.services.CategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    CategoryService categoryService;

    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<CategoryModel> getAllCategories(){
        return categoryService.getAllCategories();
    }
}
