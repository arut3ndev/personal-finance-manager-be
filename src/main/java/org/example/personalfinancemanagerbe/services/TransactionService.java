package org.example.personalfinancemanagerbe.services;

import org.example.personalfinancemanagerbe.models.CategoryModel;
import org.example.personalfinancemanagerbe.models.TransactionModel;
import org.example.personalfinancemanagerbe.repositories.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {
    private final TransactionRepository repository;
    private final CategoryService categoryService;
    TransactionService(TransactionRepository repository, CategoryService categoryService){
        this.repository = repository;
        this.categoryService = categoryService;
    }

    public Optional<TransactionModel> getTransactionById(long id){
        return repository.findById(id);
    }

    public List<TransactionModel> getAll(){
        return repository.findAll();
    }

    public TransactionModel saveTransaction(TransactionModel modelToSave){
        return repository.save(modelToSave);
    }

    public TransactionModel attachCategoryToTransaction(long transactionId, long categoryId){
        Optional<TransactionModel> transactionModel = getTransactionById(transactionId);
        Optional<CategoryModel> categoryModel = categoryService.getCategoryById(categoryId);
        if(transactionModel.isPresent() && categoryModel.isPresent()){
            TransactionModel transaction = transactionModel.get();
            transaction.setCategory(categoryModel.get());
            return repository.save(transaction);
        }
        return null;
    }

    public void deleteTransaction(long id){
        repository.deleteById(id);
    }
}
