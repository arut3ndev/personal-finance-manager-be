package org.example.personalfinancemanagerbe.dtos;

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
public class TransactionDTO {
    private Long id;
    private BigDecimal amount;
    private String description;
    private LocalDate date;
    private TransactionType type;

    public TransactionModel toModel(){
        return new TransactionModel(
            this.getId(),
            this.getAmount(),
            this.getDescription(),
            this.getDate(),
            this.getType()
        );
    }

    public TransactionDTO(TransactionModel transactionModel){
        this.setId(transactionModel.getId());
        this.setAmount(transactionModel.getAmount());
        this.setDescription(transactionModel.getDescription());
        this.setDate(transactionModel.getDate());
        this.setType(transactionModel.getType());
    }
}
