package com.app.warezilla.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.warezilla.model.Transaction;
import com.app.warezilla.model.TransactionType;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {


    List<Transaction> findAllByTimestampBetween(LocalDateTime start, LocalDateTime end);
    List<Transaction> findAllByTimestampBetweenAndType(LocalDateTime start, LocalDateTime end, TransactionType type);
}
