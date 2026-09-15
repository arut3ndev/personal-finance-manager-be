package org.example.personalfinancemanagerbe.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.personalfinancemanagerbe.dtos.TransactionDTO;
import org.example.personalfinancemanagerbe.util.TransactionType;

import java.time.LocalDate;

//@Entity(name="Transaction")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TransactionModel {
    private long id;
    private String description;
    private LocalDate date;
    private TransactionType type;

    public TransactionDTO toDTO(){
        return new TransactionDTO(
                this.getId(),
                this.getDescription(),
                this.getDate(),
                this.getType()
        );
    }
}
