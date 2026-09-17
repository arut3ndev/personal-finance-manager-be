package org.example.personalfinancemanagerbe.repositories;

import org.example.personalfinancemanagerbe.models.CategoryModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryModel, Long> {
}
