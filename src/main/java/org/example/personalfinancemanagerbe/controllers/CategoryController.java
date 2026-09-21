package org.example.personalfinancemanagerbe.controllers;

import jakarta.validation.Valid;
import org.example.personalfinancemanagerbe.dtos.CategoryRequestDTO;
import org.example.personalfinancemanagerbe.dtos.CategoryResponseDTO;
import org.example.personalfinancemanagerbe.models.CategoryModel;
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
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories(){
        return ResponseEntity.ok(categoryService.getAllCategories()
                .stream()
                .map(CategoryResponseDTO::new)
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> getCategoryById(@PathVariable Long id){
        Optional<CategoryModel> categoryModelOptional = categoryService.getCategoryById(id);
        if(categoryModelOptional.isPresent()){
            return ResponseEntity.ok(new CategoryResponseDTO(categoryModelOptional.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Void> createCategory(@Valid @RequestBody CategoryRequestDTO categoryRequestDTO){
        CategoryModel categoryModel = categoryRequestDTO.toModel();
        categoryModel = categoryService.saveCategory(categoryModel);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(categoryModel.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> putCategory(@PathVariable Long id, @Valid @RequestBody CategoryRequestDTO categoryRequestDTO){
        Optional<CategoryModel> categoryModelOptional = categoryService.getCategoryById(id);
        if(categoryModelOptional.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        CategoryModel categoryModel = categoryRequestDTO.toModel();
        categoryModel.setId(id);
        categoryModel = categoryService.saveCategory(categoryModel);
        return ResponseEntity.ok(new CategoryResponseDTO(categoryModel));
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
