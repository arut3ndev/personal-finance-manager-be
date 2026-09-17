package org.example.personalfinancemanagerbe.repositories;

import org.example.personalfinancemanagerbe.models.TransactionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionModel, Long>{
}
