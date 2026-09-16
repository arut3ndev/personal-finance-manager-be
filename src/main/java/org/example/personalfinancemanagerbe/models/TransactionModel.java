package org.example.personalfinancemanagerbe.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.personalfinancemanagerbe.util.TransactionType;

import java.time.LocalDate;

//@Entity(name="Transaction")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TransactionModel {
    private Long id;
    private Double amount;
    private String description;
    private LocalDate date;
    private TransactionType type;
}
