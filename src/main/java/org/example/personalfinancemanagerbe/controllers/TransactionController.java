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
    private final CategoryService categoryService;

    public TransactionController(TransactionService service, CategoryService categoryService){
        this.transactionService = service;
        this.categoryService = categoryService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> getTransaction(@PathVariable Long id){
        Optional<TransactionModel> transactionModel = transactionService.getTransactionById(id);
        if(transactionModel.isPresent()){
            return ResponseEntity.ok(new TransactionResponseDTO(transactionModel.get()));
        }
        return ResponseEntity.notFound().build();
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
        TransactionModel transactionModel = transactionRequestDTO.toModel();
        transactionModel = transactionService.saveTransaction(transactionModel);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(transactionModel.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> putTransaction(@PathVariable long id,@Valid @RequestBody TransactionRequestDTO transactionRequestDTO) {
        Optional<TransactionModel> existingModel = transactionService.getTransactionById(id);
        if(existingModel.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        TransactionModel transactionModel = transactionRequestDTO.toModel();
        transactionModel.setId(id);
        if(transactionRequestDTO.getCategoryId() != null){
            Optional<CategoryModel> categoryModel = categoryService.getCategoryById(transactionRequestDTO.getCategoryId());
            if(categoryModel.isEmpty()){
                return ResponseEntity.notFound().build();
            }
            transactionModel.setCategory(categoryModel.get());
        }
        transactionModel = transactionService.saveTransaction(transactionModel);
        return ResponseEntity.ok(new TransactionResponseDTO(transactionModel));
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
        Optional<TransactionModel> transactionModel = transactionService.getTransactionById(id);
        if(transactionModel.isPresent()){
            transactionService.deleteTransaction(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
