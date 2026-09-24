package org.example.personalfinancemanagerbe.services;

import org.example.personalfinancemanagerbe.exceptions.NotFoundException;
import org.example.personalfinancemanagerbe.models.CategoryModel;
import org.example.personalfinancemanagerbe.models.TransactionModel;
import org.example.personalfinancemanagerbe.repositories.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {
    private final TransactionRepository repository;
    private final CategoryService categoryService;
    public TransactionService(TransactionRepository repository, CategoryService categoryService){
        this.repository = repository;
        this.categoryService = categoryService;
    }

    public TransactionModel getTransactionById(Long id){
        Optional<TransactionModel> optionalTransactionModel = repository.findById(id);
        if(optionalTransactionModel.isEmpty()){
            throw new NotFoundException("Transaction", id);
        }
        return optionalTransactionModel.get();
    }

    public List<TransactionModel> getAll(){
        return repository.findAll();
    }

    @Transactional
    public TransactionModel saveTransaction(TransactionModel modelToSave, Long categoryId){
        if (categoryId != null){
            CategoryModel categoryModel = categoryService.getReferenceCategoryById(categoryId);
            modelToSave.setCategory(categoryModel);
        }
        return repository.save(modelToSave);
    }

    @Transactional
    public TransactionModel updateTransaction(Long transactionId, TransactionModel modelToUpdate, Long categoryId){
        TransactionModel transactionModel = repository.findById(transactionId)
                .orElseThrow(() -> new NotFoundException("Transaction", transactionId));
        transactionModel.setAmount(modelToUpdate.getAmount());
        transactionModel.setDescription(modelToUpdate.getDescription());
        transactionModel.setDate(modelToUpdate.getDate());
        transactionModel.setType(modelToUpdate.getType());
        if (categoryId != null){
            CategoryModel categoryModel = categoryService.getReferenceCategoryById(categoryId);
            transactionModel.setCategory(categoryModel);
        }
        else transactionModel.setCategory(null);
        return transactionModel;
    }

    @Transactional
    public void attachCategoryToTransaction(Long transactionId, Long categoryId) {
        TransactionModel transaction = getTransactionById(transactionId);
        transaction.setCategory(categoryService.getReferenceCategoryById(categoryId));
    }

    public void deleteTransaction(Long id){
        repository.deleteById(id);
    }
}
