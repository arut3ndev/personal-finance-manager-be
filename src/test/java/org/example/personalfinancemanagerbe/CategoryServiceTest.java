package org.example.personalfinancemanagerbe;

import org.example.personalfinancemanagerbe.exceptions.InvalidReferenceException;
import org.example.personalfinancemanagerbe.exceptions.NotFoundException;
import org.example.personalfinancemanagerbe.models.CategoryModel;
import org.example.personalfinancemanagerbe.repositories.CategoryRepository;
import org.example.personalfinancemanagerbe.services.CategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private CategoryModel category(Long id, String name) {
        return new CategoryModel(id, name, "some description", null);
    }

    @Test
    void getCategoryById_whenPresent_returnsCategory() {
        CategoryModel existing = category(1L, "Food");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(existing));

        CategoryModel result = categoryService.getCategoryById(1L);

        assertThat(result).isSameAs(existing);
    }

    @Test
    void getCategoryById_whenMissing_throwsNotFound() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.getCategoryById(99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Category")
                .hasMessageContaining("99");
    }

    @Test
    void getReferenceCategoryById_whenPresent_returnsCategory() {
        CategoryModel existing = category(1L, "Food");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(existing));

        CategoryModel result = categoryService.getReferenceCategoryById(1L);

        assertThat(result).isSameAs(existing);
    }

    @Test
    void getReferenceCategoryById_whenMissing_throwsInvalidReference() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.getReferenceCategoryById(99L))
                .isInstanceOf(InvalidReferenceException.class)
                .hasMessageContaining("Category")
                .hasMessageContaining("99");
    }

    @Test
    void updateCategory_copiesFieldsOntoManagedEntity() {
        CategoryModel existing = category(1L, "Food");
        CategoryModel changes = category(null, "Groceries");
        changes.setDescription("weekly shopping");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(existing));

        CategoryModel result = categoryService.updateCategory(changes, 1L);

        assertThat(result).isSameAs(existing);
        assertThat(existing.getName()).isEqualTo("Groceries");
        assertThat(existing.getDescription()).isEqualTo("weekly shopping");
    }

    @Test
    void updateCategory_whenMissing_throwsNotFound() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.updateCategory(category(null, "Groceries"), 99L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void getAllCategories_returnsWhatRepositoryReturns() {
        List<CategoryModel> stored = List.of(category(1L, "Food"), category(2L, "Rent"));
        when(categoryRepository.findAll()).thenReturn(stored);

        assertThat(categoryService.getAllCategories()).isEqualTo(stored);
    }

    @Test
    void saveCategory_delegatesToRepository() {
        CategoryModel toSave = category(null, "Food");
        when(categoryRepository.save(toSave)).thenReturn(category(1L, "Food"));

        CategoryModel result = categoryService.saveCategory(toSave);

        verify(categoryRepository).save(toSave);
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void deleteCategory_delegatesToRepository() {
        categoryService.deleteCategory(3L);

        verify(categoryRepository).deleteById(3L);
    }
}