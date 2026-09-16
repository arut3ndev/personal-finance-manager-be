package org.example.personalfinancemanagerbe.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.personalfinancemanagerbe.util.TransactionType;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name="transaction")
public class TransactionModel {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="transaction_id")
    private Long id;

    @Column(name="amount", nullable = false, precision = 16, scale=2)
    @NotNull
    @Digits(integer=14, fraction=2)
    private BigDecimal amount;

    @Length(max = 256)
    @Column(name="description", length = 256)
    private String description;

    @Column(name="date", nullable = false)
    @NotNull
    private LocalDate date;

    @Column(name="type", nullable = false)
    @NotNull
    @Enumerated(EnumType.STRING)
    private TransactionType type;
}
