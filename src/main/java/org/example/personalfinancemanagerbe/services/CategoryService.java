package org.example.personalfinancemanagerbe.services;

import org.example.personalfinancemanagerbe.exceptions.NotFoundException;
import org.example.personalfinancemanagerbe.models.CategoryModel;
import org.example.personalfinancemanagerbe.repositories.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryModel getCategoryById(Long id){
        Optional<CategoryModel> categoryModelOptional = categoryRepository.findById(id);
        if(categoryModelOptional.isEmpty()){
            throw new NotFoundException("Category", id);
        }
        return categoryModelOptional.get();
    }

    @Transactional
    public CategoryModel updateCategory(CategoryModel updateDummy, Long id){
        CategoryModel updatedCategoryModel = getCategoryById(id);
        updatedCategoryModel.setName(updateDummy.getName());
        updatedCategoryModel.setDescription(updateDummy.getDescription());
        return updatedCategoryModel;
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
