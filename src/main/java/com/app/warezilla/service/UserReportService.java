package com.app.warezilla.service;

import com.app.warezilla.model.Transaction;
import com.app.warezilla.model.User;
import com.app.warezilla.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class UserReportService {

    private final UserRepository userRepository;
    private final EmailService mailService;
    private final CsvService csvExportService;

    public UserReportService(
            UserRepository userRepository,
            EmailService mailService,
            CsvService csvExportService) {

        this.userRepository = userRepository;
        this.mailService = mailService;
        this.csvExportService = csvExportService;
    }

    public void sendReports(LocalDateTime start, LocalDateTime end) {
        List<User> users = userRepository.findAllByEmailIsNotNullAndReportEnabledTrue(start, end);
        users.parallelStream().forEach(this::sendReportForUser);
    }

    private void sendReportForUser(User user) {
        try {
            List<Transaction> transactions = user.getTransactionList();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            csvExportService.exportTransactionsToCsv(transactions, baos);

            mailService.sendEmail(
                    user.getEmail(),
                    baos.toByteArray()
            );

        } catch (Exception e) {
            // log & continue (never block other users)
            log.error("Failed to send report for user {}", user.getId(), e);
        }
    }
}
