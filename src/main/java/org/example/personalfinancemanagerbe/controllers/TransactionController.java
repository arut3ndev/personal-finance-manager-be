package org.example.personalfinancemanagerbe.controllers;

import org.example.personalfinancemanagerbe.dtos.CategoryDTO;
import org.example.personalfinancemanagerbe.dtos.TransactionDTO;
import org.example.personalfinancemanagerbe.models.TransactionModel;
import org.example.personalfinancemanagerbe.services.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionService transactionService;
    public TransactionController(TransactionService service){
        this.transactionService = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDTO> getTransaction(@PathVariable Long id){
        Optional<TransactionModel> transactionModel = transactionService.getTransactionById(id);
        if(transactionModel.isPresent()){
            return ResponseEntity.ok(new TransactionDTO(transactionModel.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping()
    public ResponseEntity<List<TransactionDTO>> getAllTransactions(){
        List<TransactionDTO> listOfTransactions = transactionService
                .getAll()
                .stream()
                .map(TransactionDTO::new).
                toList();
        return ResponseEntity.ok(listOfTransactions);
    }

    @PostMapping
    public ResponseEntity<Void> postTransaction(@RequestBody TransactionDTO transactionDTO){
        transactionDTO.setId(null);
        TransactionModel transactionModel = transactionDTO.toModel();
        transactionModel = transactionService.saveTransaction(transactionModel);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(transactionModel.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionDTO> putTransaction(@PathVariable long id, @RequestBody TransactionDTO transactionDTO) {
        Optional<TransactionModel> existingModel = transactionService.getTransactionById(id);
        if(existingModel.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        TransactionModel transactionModel = transactionDTO.toModel();
        transactionModel.setId(id);
        transactionModel = transactionService.saveTransaction(transactionModel);
        return ResponseEntity.ok(new TransactionDTO(transactionModel));
    }

    @PutMapping("/{id}/category")
    public ResponseEntity<Void> attachCategoryToTransaction(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO){
        TransactionModel updatedModel = transactionService.attachCategoryToTransaction(id, categoryDTO.toModel());
        if(updatedModel != null){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id){
        Optional<TransactionModel> transactionModel = transactionService.getTransactionById(id);
        if(transactionModel.isPresent()){
            transactionService.deleteTransaction(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
