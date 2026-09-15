package org.example.personalfinancemanagerbe.repositories;

import org.example.personalfinancemanagerbe.models.TransactionModel;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
public class TransactionRepository {
    HashMap<Long, TransactionModel> database;
    private long idCounter;
    public TransactionRepository(){
        this.database = new HashMap<>();
        this.idCounter = 1;
    }

    public TransactionModel save(TransactionModel modelToSave){
        if(modelToSave != null){
            if(database.get(modelToSave.getId()) == null){
                modelToSave.setId(idCounter);
                database.put(idCounter, modelToSave);
                idCounter++;
                return modelToSave;
            }
            else{
                database.put(modelToSave.getId(), modelToSave);
            }
        }
        return null;
    }

    public Optional<TransactionModel> findById(long id){
        return Optional.ofNullable(database.get(id));
    }

    public List<TransactionModel> findAll(){
        return database.values().stream().toList();
    }

    public void deleteById(long id){
        database.remove(id);
    }
}
