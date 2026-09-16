package org.example.personalfinancemanagerbe.services;

import org.example.personalfinancemanagerbe.models.TransactionModel;
import org.example.personalfinancemanagerbe.repositories.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {
    private final TransactionRepository repository;
    TransactionService(TransactionRepository repository){
        this.repository = repository;
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

    public void deleteTransaction(long id){
        repository.deleteById(id);
    }
}
