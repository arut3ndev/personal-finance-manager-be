package org.example.personalfinancemanagerbe.controllers;

import org.example.personalfinancemanagerbe.dtos.TransactionDTO;
import org.example.personalfinancemanagerbe.models.TransactionModel;
import org.example.personalfinancemanagerbe.services.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("api/transaction")
public class TransactionController {
    TransactionService transactionService;
    public TransactionController(TransactionService service){
        this.transactionService = service;
    }

    @GetMapping("{id}")
    public ResponseEntity<TransactionDTO> getTransaction(@PathVariable Long id){
        Optional<TransactionModel> transactionModel = transactionService.getTransactionById(id);
        if(transactionModel.isPresent()){
            return ResponseEntity.ok(transactionModel.get().toDTO());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Void> postTransaction(@RequestBody TransactionDTO transactionDTO){
        TransactionModel transactionModel = transactionDTO.toModel();
        transactionModel = transactionService.saveTransaction(transactionModel);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(transactionModel.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("{id}")
    public ResponseEntity<TransactionModel> putTransaction(@PathVariable long id, @RequestBody TransactionDTO transactionDTO) {
        TransactionModel transactionModel = transactionDTO.toModel();
        Optional<TransactionModel> existingModel = transactionService.getTransactionById(id);
        if (existingModel.isPresent() && existingModel.get().getId() == transactionModel.getId()) {
            transactionModel = transactionService.saveTransaction(transactionModel);
                return ResponseEntity.ok(transactionModel);
        }
        else if (existingModel.isPresent() && existingModel.get().getId() != transactionModel.getId()) {
                return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id){
        Optional<TransactionModel> transactionModel = transactionService.getTransactionById(id);
        if(transactionModel.isPresent()){
            transactionService.deleteTransaction(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
