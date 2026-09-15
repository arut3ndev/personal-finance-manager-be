package org.example.personalfinancemanagerbe.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.personalfinancemanagerbe.models.TransactionModel;
import org.example.personalfinancemanagerbe.util.TransactionType;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TransactionDTO {
    private long id;
    private String description;
    private LocalDate date;
    private TransactionType type;

    public TransactionModel toModel(){
        return new TransactionModel(
                this.getId(),
                this.getDescription(),
                this.getDate(),
                this.getType()
        );
    }
}
