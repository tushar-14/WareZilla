package com.app.warezilla.controller;

import com.app.warezilla.model.Summary;
import com.app.warezilla.model.Transaction;
import com.app.warezilla.service.TransactionService;
import com.app.warezilla.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    //add a transaction
    @PostMapping()
    public ResponseEntity<Transaction> addTransaction(@RequestBody Transaction transaction) {
    	try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            service.addTransaction(transaction, authentication.getName());
    		return new ResponseEntity<Transaction>(transaction, HttpStatus.OK);
    		
		} catch (Exception e) {
            System.out.println(e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
    }

    //get transaction for current date
    @GetMapping()
    public ResponseEntity<List<Transaction>> getTodayTransactions() {
    	
    	try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    		return new ResponseEntity<List<Transaction>>(service.getTransactionsForToday(authentication.getName()), HttpStatus.OK) ;
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
        
    }

    //get transactions for a selected date
    @GetMapping("/history")
    public ResponseEntity<List<Transaction>> getTransactionsByDate(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
    	try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    		return new ResponseEntity<List<Transaction>>(service.getTransactionsForDate(date, authentication.getName()), HttpStatus.OK) ;
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
    	
    }

    //get total summary for current date
    @GetMapping("/summary")
    public ResponseEntity<Summary> getTodaySummary(@PathVariable String userName) {
    	
    	try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    		return new ResponseEntity<>(service.getTodaySummary(authentication.getName()), HttpStatus.OK) ;
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
    }

    //delete a transaction
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteTransaction(@PathVariable Long id){
        try {
            service.deleteTransaction(id);
            return new ResponseEntity<>(HttpStatus.OK) ;
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
