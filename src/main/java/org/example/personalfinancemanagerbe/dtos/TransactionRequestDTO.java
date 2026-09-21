package org.example.personalfinancemanagerbe.dtos;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
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
public class TransactionRequestDTO {
    @NotNull
    @Positive()
    @Digits(integer = 16, fraction = 2)
    private BigDecimal amount;
    @Size(max = 256)
    private String description;
    @NotNull
    private LocalDate date;
    @NotNull
    private TransactionType type;
    private Long categoryId;

    public TransactionModel toModel(){
        return new TransactionModel(
            null,
            this.getAmount(),
            this.getDescription(),
            this.getDate(),
            this.getType(),
            null
        );
    }
}
