package com.app.warezilla.repository;

import com.app.warezilla.model.Transaction;
import com.app.warezilla.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {


    List<Transaction> findAllByTimestampBetweenAndUserId(LocalDateTime start, LocalDateTime end, Long id);
    List<Transaction> findAllByTimestampBetweenAndType(LocalDateTime start, LocalDateTime end, TransactionType type);
}
