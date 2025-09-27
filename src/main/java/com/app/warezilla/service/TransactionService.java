package com.app.warezilla.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.warezilla.model.Transaction;
import com.app.warezilla.model.TransactionType;
import com.app.warezilla.repository.TransactionRepository;

@Service
public class TransactionService {

	@Autowired
    TransactionRepository repository;

    public Transaction addTransaction(Transaction transaction) {
        if (transaction.getTimestamp() == null) {
            transaction.setTimestamp(null);
            
        }
        return repository.save(transaction);
    }

    public List<Transaction> getTransactionsForDate(LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);
        return repository.findAllByTimestampBetween(start, end);
    }

    public List<Transaction> getTransactionsForToday() {
        return getTransactionsForDate(LocalDate.now());
    }

    public Summary getTodaySummary() {
        List<Transaction> transactions = getTransactionsForToday();
        int totalIn = transactions.stream().filter(t -> t.getType() == TransactionType.IN).mapToInt(t -> t.getQuantity()).sum();
        int totalOut = transactions.stream().filter(t -> t.getType() == TransactionType.OUT).mapToInt(t -> t.getQuantity()).sum();
        return new Summary(totalIn, totalOut);
    }

    public static class Summary {
        public int totalIn;
        public int totalOut;

        public Summary(int in, int out) {
            this.totalIn = in;
            this.totalOut = out;
        }
    }
}
