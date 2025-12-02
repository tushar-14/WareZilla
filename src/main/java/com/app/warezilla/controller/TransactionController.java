package com.app.warezilla.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
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
import com.app.warezilla.service.TransactionService.Summary;

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
    public ResponseEntity<Transaction> addTransaction(@RequestBody Transaction transaction) {
    	try {
    		return new ResponseEntity<Transaction>(service.addTransaction(transaction), HttpStatus.OK);
    		
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
    }

    @GetMapping("/transactions/today")
    public ResponseEntity<List<Transaction>> getTodayTransactions() {
    	
    	try {
    		return new ResponseEntity<List<Transaction>>(service.getTransactionsForToday(), HttpStatus.OK) ;
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
        
    }

    @GetMapping("/transactions/history")
    public ResponseEntity<List<Transaction>> getTransactionsByDate(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
    	try {
    		return new ResponseEntity<List<Transaction>>(service.getTransactionsForDate(date), HttpStatus.OK) ;
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
    	
    }

    @GetMapping("/summary/today")
    public ResponseEntity<Summary> getTodaySummary() {
    	
    	try {
    		return new ResponseEntity<>(service.getTodaySummary(), HttpStatus.OK) ;
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
    }
}
