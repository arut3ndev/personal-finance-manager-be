package org.example.personalfinancemanagerbe;

import org.example.personalfinancemanagerbe.models.CategoryModel;
import org.example.personalfinancemanagerbe.repositories.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DataJpaTest
public class CategoryRepositoryTest {
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    TestEntityManager entityManager;

    private CategoryModel category(Long id) {
        return new CategoryModel(id, "Food", "some description", null);
    }

    @Test
    public void save_testUniqueConstraint(){
        CategoryModel categoryModel = category(null);
        categoryRepository.save(categoryModel);
        entityManager.flush();
        entityManager.clear();
        CategoryModel categoryModel1 = category(null);
        assertThatThrownBy(() -> categoryRepository.saveAndFlush(categoryModel1)).isInstanceOf(Exception.class);
    }
}
