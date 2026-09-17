package org.example.personalfinancemanagerbe.repositories;

import org.example.personalfinancemanagerbe.models.TransactionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionModel, Long>{
}
