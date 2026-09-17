package org.example.personalfinancemanagerbe.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.List;
import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "categories")
public class CategoryModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Column(name = "category_name", nullable = false, length = 32)
    private String name;

    @Column(name="category_description", nullable = false, length = 256)
    private String description;

    @OneToMany(fetch = FetchType.LAZY)
    List<TransactionModel> transaction;

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
        return "CategoryModel{id=" + id
                + ", name=" + name
                + ", description=" + description + "}";
    }
}
