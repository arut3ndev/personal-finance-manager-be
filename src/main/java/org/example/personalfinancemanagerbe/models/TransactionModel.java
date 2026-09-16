package org.example.personalfinancemanagerbe.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.personalfinancemanagerbe.util.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity()
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
    private BigDecimal amount;
    @Column(name="description", length = 256)
    private String description;
    @Column(name="date", nullable = false)
    private LocalDate date;
    @Column(name="type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType type;
}
