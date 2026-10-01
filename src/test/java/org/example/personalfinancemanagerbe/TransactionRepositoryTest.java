package org.example.personalfinancemanagerbe;

import org.example.personalfinancemanagerbe.models.CategoryModel;
import org.example.personalfinancemanagerbe.models.TransactionModel;
import org.example.personalfinancemanagerbe.repositories.TransactionRepository;
import org.example.personalfinancemanagerbe.util.TransactionType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class TransactionRepositoryTest {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TestEntityManager entityManager;

    private TransactionModel transaction(Long id, CategoryModel categoryModel) {
        return new TransactionModel(id, new BigDecimal("42.00"), "groceries",
                LocalDate.of(2026, 3, 1), TransactionType.OUTGOING, categoryModel);
    }

    private CategoryModel category(Long id){
        return new CategoryModel(
                id,
                "Food",
                "Food category",
                null
        );
    }

    @Test
    void save_thenFindById_returnsAllFieldsIntact() {

        TransactionModel saved = transaction(null, null);
        transactionRepository.save(saved);
        entityManager.flush();
        entityManager.clear();

        List<TransactionModel> list = transactionRepository.findAll();
        assertThat(!list.isEmpty());
        Optional<TransactionModel> found = transactionRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getAmount()).isEqualByComparingTo("42.00");
        assertThat(found.get().getDescription()).isEqualTo("groceries");
        assertThat(found.get().getDate()).isEqualTo(LocalDate.of(2026, 3, 1));
        assertThat(found.get().getType()).isEqualTo(TransactionType.OUTGOING);
    }

    @Test
    void save_persistsCategoryRelationship() {
        CategoryModel persistedCategory = category(null);
        entityManager.persistFlushFind(persistedCategory);
        TransactionModel saved = transaction(null, persistedCategory);
        transactionRepository.save(saved);

        entityManager.flush();
        entityManager.clear();

        Optional<TransactionModel> found = transactionRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getCategory()).isNotNull();
        assertThat(found.get().getCategory().getName()).isEqualTo("Food");
        assertThat(found.get().getCategory().getDescription()).isEqualTo("Food category");
    }
}
