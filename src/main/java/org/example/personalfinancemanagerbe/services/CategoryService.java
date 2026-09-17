package org.example.personalfinancemanagerbe.services;

import org.example.personalfinancemanagerbe.models.CategoryModel;
import org.example.personalfinancemanagerbe.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Optional<CategoryModel> getCategoryById(Long id){
        return categoryRepository.findById(id);
    }

    public List<CategoryModel> getAllCategories(){
        return categoryRepository.findAll();
    }

    public CategoryModel saveCategory(CategoryModel category){
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id){
        categoryRepository.deleteById(id);
    }
}
