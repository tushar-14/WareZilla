package com.app.warezilla.controller;

import com.app.warezilla.model.Summary;
import com.app.warezilla.model.Transaction;
import com.app.warezilla.service.TransactionService;
import com.app.warezilla.service.UserService;
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
//@CrossOrigin(origins = "*")
public class TransactionController {

    @Autowired
    private TransactionService service;

    @Autowired
    private UserService userService;

    @GetMapping("/check")
    public CsrfToken csrf(CsrfToken token) {
        return token;
    }
    
    @PostMapping("/{userName}")
    public ResponseEntity<Transaction> addTransaction(@RequestBody Transaction transaction , @PathVariable String userName) {
    	try {
            service.addTransaction(transaction,userName);
    		return new ResponseEntity<Transaction>(transaction, HttpStatus.OK);
    		
		} catch (Exception e) {
            System.out.println(e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
    }

    @GetMapping("/{userName}")
    public ResponseEntity<List<Transaction>> getTodayTransactions(@PathVariable String userName) {
    	
    	try {
    		return new ResponseEntity<List<Transaction>>(service.getTransactionsForToday(userName), HttpStatus.OK) ;
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
        
    }

    @GetMapping("/history/{userName}")
    public ResponseEntity<List<Transaction>> getTransactionsByDate(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, @PathVariable String userName) {
        
    	try {
    		return new ResponseEntity<List<Transaction>>(service.getTransactionsForDate(date,userName), HttpStatus.OK) ;
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
    	
    }

    @GetMapping("/summary/{userName}")
    public ResponseEntity<Summary> getTodaySummary(@PathVariable String userName) {
    	
    	try {
    		return new ResponseEntity<>(service.getTodaySummary(userName), HttpStatus.OK) ;
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteTransaction(@PathVariable Long id){
        try {
            service.deleteTransaction(id);
            return new ResponseEntity<>(HttpStatus.OK) ;
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST  );
        }
    }
}
