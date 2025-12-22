package com.app.warezilla.service;

import com.app.warezilla.model.Summary;
import com.app.warezilla.model.Transaction;
import com.app.warezilla.model.TransactionType;
import com.app.warezilla.model.User;
import com.app.warezilla.repository.TransactionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTests {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private TransactionService transactionService;

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void addTransactionSetsTimestampAndUserAndSaves() {
        Transaction tx = new Transaction();
        tx.setQuantity(5);
        tx.setType(TransactionType.IN);

        User user = new User();
        user.setId(10L);
        when(userService.findByUserName("john")).thenReturn(user);

        ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
        transactionService.addTransaction(tx, "john");

        verify(transactionRepository).save(captor.capture());
        Transaction saved = captor.getValue();

        assertNotNull(saved.getTimestamp());
        assertEquals(user, saved.getUser());
    }

    @Test
    void getTransactionsForDateReturnsRepoResults() {
        LocalDate date = LocalDate.of(2025, 1, 1);
        User user = new User();
        user.setId(3L);
        when(userService.findByUserName("joe")).thenReturn(user);

        Transaction t1 = new Transaction();
        t1.setId(1L);
        Transaction t2 = new Transaction();
        t2.setId(2L);
        List<Transaction> list = Arrays.asList(t1, t2);

        when(transactionRepository.findAllByTimestampBetweenAndUserId(
                any(LocalDateTime.class), any(LocalDateTime.class), eq(3L)
        )).thenReturn(list);

        List<Transaction> result = transactionService.getTransactionsForDate(date, "joe");

        assertEquals(list, result);
        verify(transactionRepository).findAllByTimestampBetweenAndUserId(any(LocalDateTime.class),
                any(LocalDateTime.class), eq(3L));
    }

    @Test
    void getTransactionsForTodayDelegatesToRepository() {
        User user = new User();
        user.setId(4L);
        when(userService.findByUserName("todayUser")).thenReturn(user);

        Transaction t = new Transaction();
        t.setId(11L);
        List<Transaction> list = Arrays.asList(t);

        when(transactionRepository.findAllByTimestampBetweenAndUserId(
                any(LocalDateTime.class), any(LocalDateTime.class), eq(4L)
        )).thenReturn(list);

        List<Transaction> result = transactionService.getTransactionsForToday("todayUser");

        assertEquals(list, result);
        // optional check that the date range covers the whole day
        ArgumentCaptor<LocalDateTime> startCap = ArgumentCaptor.forClass(LocalDateTime.class);
        ArgumentCaptor<LocalDateTime> endCap = ArgumentCaptor.forClass(LocalDateTime.class);
        verify(transactionRepository).findAllByTimestampBetweenAndUserId(startCap.capture(), endCap.capture(), eq(4L));
        LocalDateTime start = startCap.getValue();
        LocalDateTime end = endCap.getValue();
        assertEquals(LocalTime.MIDNIGHT, start.toLocalTime());
        assertTrue(end.toLocalTime().isAfter(start.toLocalTime()));
    }

    @Test
    void getTodaySummaryCalculatesTotalsCorrectly() {
        User user = new User();
        user.setId(5L);
        when(userService.findByUserName("sumUser")).thenReturn(user);

        Transaction in1 = new Transaction();
        in1.setType(TransactionType.IN);
        in1.setQuantity(10);

        Transaction in2 = new Transaction();
        in2.setType(TransactionType.IN);
        in2.setQuantity(5);

        Transaction out1 = new Transaction();
        out1.setType(TransactionType.OUT);
        out1.setQuantity(3);

        List<Transaction> list = Arrays.asList(in1, in2, out1);

        when(transactionRepository.findAllByTimestampBetweenAndUserId(
                any(LocalDateTime.class), any(LocalDateTime.class), eq(5L)
        )).thenReturn(list);

        Summary summary = transactionService.getTodaySummary("sumUser");

        assertEquals(15, summary.getTotalIn());
        assertEquals(3, summary.getTotalOut());
    }

    @Test
    void deleteTransactionDeletesWhenUserOwnsTransaction() {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("sam");
        SecurityContext ctx = mock(SecurityContext.class);
        when(ctx.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(ctx);

        User user = new User();
        Transaction owned = new Transaction();
        owned.setId(7L);
        user.setTransactionList(new ArrayList<>());
        user.getTransactionList().add(owned);

        when(userService.findByUserName("sam")).thenReturn(user);

        transactionService.deleteTransaction(7L);

        verify(transactionRepository).deleteById(7L);
    }

    @Test
    void deleteTransactionDoesNotDeleteWhenUserDoesNotOwnTransaction() {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("sam");
        SecurityContext ctx = mock(SecurityContext.class);
        when(ctx.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(ctx);

        User user = new User();
        user.setTransactionList(new ArrayList<>()); // empty list

        when(userService.findByUserName("sam")).thenReturn(user);

        transactionService.deleteTransaction(99L);

        verify(transactionRepository, never()).deleteById(anyLong());
    }
}

