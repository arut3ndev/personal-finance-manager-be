package org.example.personalfinancemanagerbe.repositories;

import org.example.personalfinancemanagerbe.models.TransactionModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class TransactionRepository {
    private final ConcurrentHashMap<Long, TransactionModel> database;
    private final AtomicLong idCounter;
    public TransactionRepository(){
        this.database = new ConcurrentHashMap<>();
        this.idCounter = new AtomicLong(1);
    }

    public TransactionModel save(TransactionModel modelToSave){
        if (modelToSave.getId() == null || !database.containsKey(modelToSave.getId())) {
            long newId = idCounter.getAndIncrement();
            modelToSave.setId(newId);
            database.put(newId, modelToSave);
        }
        else {
            database.put(modelToSave.getId(), modelToSave);
        }
        return modelToSave;
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
