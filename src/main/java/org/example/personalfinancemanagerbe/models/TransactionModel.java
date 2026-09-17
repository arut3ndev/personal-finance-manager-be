package org.example.personalfinancemanagerbe.models;

import jakarta.persistence.*;
import lombok.*;
import org.example.personalfinancemanagerbe.util.TransactionType;
import org.hibernate.Hibernate;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name="transactions")
public class TransactionModel {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="transaction_id")
    private Long id;

    @Column(name="transaction_amount", nullable = false, precision = 16, scale=2)
    private BigDecimal amount;

    @Column(name="transaction_description", length = 256)
    private String description;

    @Column(name="transaction_date", nullable = false)
    private LocalDate date;

    @Column(name="transaction_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @ManyToOne
    @JoinColumn(name = "category_id")
    CategoryModel category;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> thisClass = Hibernate.getClass(this);
        Class<?> otherClass = Hibernate.getClass(o);
        if (thisClass != otherClass) return false;
        TransactionModel that = (TransactionModel) o;
        return id != null && Objects.equals(id, that.getId());
    }

    @Override
    public int hashCode() {
        return Hibernate.getClass(this).hashCode();
    }

    @Override
    public String toString() {
        return "TransactionModel{id=" + id
                + ", amount=" + amount
                + ", description=" + description
                + ", date=" + date
                + ", type=" + type + "}";
    }
}
