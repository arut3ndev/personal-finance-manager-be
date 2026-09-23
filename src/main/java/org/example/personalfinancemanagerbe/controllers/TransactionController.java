package org.example.personalfinancemanagerbe.controllers;

import jakarta.validation.Valid;
import org.example.personalfinancemanagerbe.dtos.TransactionRequestDTO;
import org.example.personalfinancemanagerbe.dtos.TransactionResponseDTO;
import org.example.personalfinancemanagerbe.models.CategoryModel;
import org.example.personalfinancemanagerbe.models.TransactionModel;
import org.example.personalfinancemanagerbe.services.CategoryService;
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
    public ResponseEntity<TransactionResponseDTO> getTransaction(@PathVariable Long id){
        return ResponseEntity.ok(new TransactionResponseDTO(transactionService.getTransactionById(id)));
    }

    @GetMapping()
    public ResponseEntity<List<TransactionResponseDTO>> getAllTransactions(){
        List<TransactionResponseDTO> listOfTransactions = transactionService
                .getAll()
                .stream()
                .map(TransactionResponseDTO::new).
                toList();
        return ResponseEntity.ok(listOfTransactions);
    }

    @PostMapping
    public ResponseEntity<Void> postTransaction(@Valid @RequestBody TransactionRequestDTO transactionRequestDTO){
        TransactionModel transactionModel = transactionService.saveTransaction(transactionRequestDTO.toModel(), transactionRequestDTO.getCategoryId());
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(transactionModel.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> putTransaction(@PathVariable Long id, @Valid @RequestBody TransactionRequestDTO transactionRequestDTO) {
        return ResponseEntity.ok(new TransactionResponseDTO(
            transactionService.updateTransaction(
                id,
                transactionRequestDTO.toModel(),
                transactionRequestDTO.getCategoryId()
            )
        ));
    }

    @PutMapping("/{transactionId}/category/{categoryId}")
    public ResponseEntity<Void> attachCategoryToTransaction(@PathVariable Long transactionId, @PathVariable Long categoryId){
        TransactionModel updatedModel = transactionService.attachCategoryToTransaction(transactionId, categoryId);
        if(updatedModel != null){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id){
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }
}
