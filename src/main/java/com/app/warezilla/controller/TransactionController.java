package com.app.warezilla.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.warezilla.model.Transaction;
import com.app.warezilla.service.TransactionService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class TransactionController {

    @Autowired
    private TransactionService service;

    @GetMapping("/check")
    public CsrfToken csrf(CsrfToken token) {
        return token;
    }
    
    @PostMapping("/transactions")
    public Transaction addTransaction(@RequestBody Transaction transaction) {
        return service.addTransaction(transaction);
    }

    @GetMapping("/transactions/today")
    public List<Transaction> getTodayTransactions() {
        return service.getTransactionsForToday();
    }

    @GetMapping("/transactions/history")
    public List<Transaction> getTransactionsByDate(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return service.getTransactionsForDate(date);
    }

    @GetMapping("/summary/today")
    public TransactionService.Summary getTodaySummary() {
        return service.getTodaySummary();
    }
}
