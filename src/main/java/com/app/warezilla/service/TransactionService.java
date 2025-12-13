package com.app.warezilla.service;

import com.app.warezilla.model.Summary;
import com.app.warezilla.model.Transaction;
import com.app.warezilla.model.TransactionType;
import com.app.warezilla.model.User;
import com.app.warezilla.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class TransactionService {

	@Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public void addTransaction(Transaction transaction , String userName) {
        if (transaction.getTimestamp() == null) {
            transaction.setTimestamp(LocalDateTime.now());
        }
        User user = userService.findByUserName(userName);
        transaction.setUser(user);
        transactionRepository.save(transaction);
    }

    public List<Transaction> getTransactionsForDate(LocalDate date , String userName) {
        User user = userService.findByUserName(userName);
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);
        return transactionRepository.findAllByTimestampBetweenAndUserId(start, end, user.getId());
    }

    public List<Transaction> getTransactionsForToday(String userName) {
        return getTransactionsForDate(LocalDate.now() , userName);
    }

    public Summary getTodaySummary(String userName) {
        List<Transaction> transactions = getTransactionsForToday(userName);
        int totalIn = transactions.stream().filter(t -> t.getType() == TransactionType.IN).mapToInt(Transaction::getQuantity).sum();
        int totalOut = transactions.stream().filter(t -> t.getType() == TransactionType.OUT).mapToInt(Transaction::getQuantity).sum();
        return new Summary(totalIn, totalOut);
    }

    public void deleteTransaction(Long id){
        transactionRepository.deleteById(id);
    }
}
