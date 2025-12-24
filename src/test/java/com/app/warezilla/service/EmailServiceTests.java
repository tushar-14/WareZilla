package com.app.warezilla.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailServiceTests {

    @Autowired
    private EmailService emailService;

    @Test
    public void emailTests(){
        emailService.sendEmail("monikavij41@gmail.com","Test Subject","This is a test email body.");
    }
}


