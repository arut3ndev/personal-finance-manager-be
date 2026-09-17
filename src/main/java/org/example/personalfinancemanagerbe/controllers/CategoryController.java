package org.example.personalfinancemanagerbe.controllers;

import org.example.personalfinancemanagerbe.dtos.CategoryDTO;
import org.example.personalfinancemanagerbe.dtos.TransactionDTO;
import org.example.personalfinancemanagerbe.models.CategoryModel;
import org.example.personalfinancemanagerbe.models.TransactionModel;
import org.example.personalfinancemanagerbe.services.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories(){
        return ResponseEntity.ok(categoryService.getAllCategories()
                .stream()
                .map(CategoryDTO::new)
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id){
        Optional<CategoryModel> categoryModelOptional = categoryService.getCategoryById(id);
        if(categoryModelOptional.isPresent()){
            return ResponseEntity.ok(new CategoryDTO(categoryModelOptional.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Void> createCategory(@RequestBody CategoryDTO categoryDTO){
        categoryDTO.setId(null);
        CategoryModel categoryModel = categoryDTO.toModel();
        categoryModel = categoryService.saveCategory(categoryModel);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(categoryModel.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> putCategory(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO){
        Optional<CategoryModel> categoryModelOptional = categoryService.getCategoryById(id);
        if(categoryModelOptional.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        categoryDTO.setId(id);
        CategoryModel categoryModel = categoryDTO.toModel();
        categoryModel = categoryService.saveCategory(categoryModel);
        return ResponseEntity.ok(new CategoryDTO(categoryModel));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id){
        Optional<CategoryModel> categoryModelOptional = categoryService.getCategoryById(id);
        if(categoryModelOptional.isPresent()){
            categoryService.deleteCategory(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


}
