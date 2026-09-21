package org.example.personalfinancemanagerbe.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.personalfinancemanagerbe.models.TransactionModel;
import org.example.personalfinancemanagerbe.util.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TransactionResponseDTO {
    private Long transactionId;
    private BigDecimal amount;
    private String description;
    private LocalDate date;
    private TransactionType type;
    private Long categoryId;

    public TransactionResponseDTO(TransactionModel transactionModel){
        this.setTransactionId(transactionModel.getId());
        this.setAmount(transactionModel.getAmount());
        this.setDescription(transactionModel.getDescription());
        this.setDate(transactionModel.getDate());
        this.setType(transactionModel.getType());
        this.setCategoryId(transactionModel.getCategory() == null ? null : transactionModel.getCategory().getId());
    }
}
