package com.app.warezilla.controller;

import com.app.warezilla.model.Summary;
import com.app.warezilla.model.Transaction;
import com.app.warezilla.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transaction")
@CrossOrigin(origins = "*")
public class TransactionController {

    @Autowired
    private TransactionService service;

    @GetMapping("/check")
    public CsrfToken csrf(CsrfToken token) {
        return token;
    }
    
    @PostMapping()
    public ResponseEntity<Transaction> addTransaction(@RequestBody Transaction transaction) {
    	try {
    		return new ResponseEntity<Transaction>(service.addTransaction(transaction), HttpStatus.OK);
    		
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
    }

    @GetMapping("/today")
    public ResponseEntity<List<Transaction>> getTodayTransactions() {
    	
    	try {
    		return new ResponseEntity<List<Transaction>>(service.getTransactionsForToday(), HttpStatus.OK) ;
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
        
    }

    @GetMapping("/history")
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
