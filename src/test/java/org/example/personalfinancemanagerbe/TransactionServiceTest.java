package org.example.personalfinancemanagerbe.services;

import org.example.personalfinancemanagerbe.exceptions.InvalidReferenceException;
import org.example.personalfinancemanagerbe.exceptions.NotFoundException;
import org.example.personalfinancemanagerbe.models.CategoryModel;
import org.example.personalfinancemanagerbe.models.TransactionModel;
import org.example.personalfinancemanagerbe.repositories.TransactionRepository;
import org.example.personalfinancemanagerbe.util.TransactionType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository repository;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private TransactionService transactionService;

    @Captor
    private ArgumentCaptor<TransactionModel> transactionCaptor;

    private TransactionModel transaction(Long id) {
        return new TransactionModel(id, new BigDecimal("42.00"), "groceries",
                LocalDate.of(2026, 3, 1), TransactionType.OUTGOING, null);
    }

    private CategoryModel category(Long id) {
        return new CategoryModel(id, "Food", "some description", null);
    }

    // --- getTransactionById ---

    @Test
    void getTransactionById_whenPresent_returnsTransaction() {
        TransactionModel existing = transaction(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(existing));

        TransactionModel result = transactionService.getTransactionById(1L);

        assertThat(result).isSameAs(existing);
    }

    @Test
    void getTransactionById_whenMissing_throwsNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transactionService.getTransactionById(99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Transaction")
                .hasMessageContaining("99");
    }

    // --- getAll ---

    @Test
    void getAll_returnsWhatRepositoryReturns() {
        List<TransactionModel> stored = List.of(transaction(1L), transaction(2L));
        when(repository.findAll()).thenReturn(stored);

        assertThat(transactionService.getAll()).isEqualTo(stored);
    }

    // --- saveTransaction ---

    @Test
    void saveTransaction_withNullCategoryId_savesWithoutCategory() {
        TransactionModel toSave = transaction(null);
        when(repository.save(toSave)).thenReturn(toSave);

        transactionService.saveTransaction(toSave, null);

        verify(categoryService, never()).getReferenceCategoryById(any());
        verify(repository).save(transactionCaptor.capture());
        assertThat(transactionCaptor.getValue().getCategory()).isNull();
    }

    @Test
    void saveTransaction_withValidCategoryId_attachesCategory() {
        TransactionModel toSave = transaction(null);
        CategoryModel food = category(7L);
        when(categoryService.getReferenceCategoryById(7L)).thenReturn(food);
        when(repository.save(toSave)).thenReturn(toSave);

        transactionService.saveTransaction(toSave, 7L);

        verify(repository).save(transactionCaptor.capture());
        assertThat(transactionCaptor.getValue().getCategory()).isSameAs(food);
    }

    @Test
    void saveTransaction_withUnknownCategoryId_throwsAndNeverSaves() {
        TransactionModel toSave = transaction(null);
        when(categoryService.getReferenceCategoryById(404L))
                .thenThrow(new InvalidReferenceException("Category", 404L));

        assertThatThrownBy(() -> transactionService.saveTransaction(toSave, 404L))
                .isInstanceOf(InvalidReferenceException.class);

        verify(repository, never()).save(any());
    }

    // --- updateTransaction ---

    @Test
    void updateTransaction_copiesFieldsOntoManagedEntity() {
        TransactionModel existing = transaction(1L);
        TransactionModel changes = new TransactionModel(null, new BigDecimal("99.99"), "rent",
                LocalDate.of(2026, 4, 1), TransactionType.INCOMING, null);
        when(repository.findById(1L)).thenReturn(Optional.of(existing));

        TransactionModel result = transactionService.updateTransaction(1L, changes, null);

        assertThat(result).isSameAs(existing);
        assertThat(existing.getAmount()).isEqualByComparingTo("99.99");
        assertThat(existing.getDescription()).isEqualTo("rent");
        assertThat(existing.getDate()).isEqualTo(LocalDate.of(2026, 4, 1));
        assertThat(existing.getType()).isEqualTo(TransactionType.INCOMING);
    }

    @Test
    void updateTransaction_relaysOnDirtyCheckingInsteadOfSave() {
        TransactionModel existing = transaction(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(existing));

        transactionService.updateTransaction(1L, transaction(null), null);

        verify(repository, never()).save(any());
    }

    @Test
    void updateTransaction_withNullCategoryId_clearsExistingCategory() {
        TransactionModel existing = transaction(1L);
        existing.setCategory(category(7L));
        when(repository.findById(1L)).thenReturn(Optional.of(existing));

        transactionService.updateTransaction(1L, transaction(null), null);

        assertThat(existing.getCategory()).isNull();
    }

    @Test
    void updateTransaction_withValidCategoryId_attachesCategory() {
        TransactionModel existing = transaction(1L);
        CategoryModel food = category(7L);
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(categoryService.getReferenceCategoryById(7L)).thenReturn(food);

        transactionService.updateTransaction(1L, transaction(null), 7L);

        assertThat(existing.getCategory()).isSameAs(food);
    }

    @Test
    void updateTransaction_whenMissing_throwsNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transactionService.updateTransaction(99L, transaction(null), null))
                .isInstanceOf(NotFoundException.class);
    }

    // --- attachCategoryToTransaction ---

    @Test
    void attachCategoryToTransaction_setsCategoryOnManagedEntity() {
        TransactionModel existing = transaction(1L);
        CategoryModel food = category(7L);
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(categoryService.getReferenceCategoryById(7L)).thenReturn(food);

        transactionService.attachCategoryToTransaction(1L, 7L);

        assertThat(existing.getCategory()).isSameAs(food);
        verify(repository, never()).save(any());
    }

    @Test
    void attachCategoryToTransaction_whenTransactionMissing_throwsNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transactionService.attachCategoryToTransaction(99L, 7L))
                .isInstanceOf(NotFoundException.class);

        verify(categoryService, never()).getReferenceCategoryById(any());
    }

    @Test
    void attachCategoryToTransaction_whenCategoryMissing_throwsInvalidReference() {
        when(repository.findById(1L)).thenReturn(Optional.of(transaction(1L)));
        when(categoryService.getReferenceCategoryById(404L))
                .thenThrow(new InvalidReferenceException("Category", 404L));

        assertThatThrownBy(() -> transactionService.attachCategoryToTransaction(1L, 404L))
                .isInstanceOf(InvalidReferenceException.class);
    }


    @Test
    void deleteTransaction_delegatesToRepository() {
        transactionService.deleteTransaction(3L);

        verify(repository).deleteById(3L);
    }
}