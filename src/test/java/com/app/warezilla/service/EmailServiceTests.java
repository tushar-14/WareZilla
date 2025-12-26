package com.app.warezilla.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailServiceTests {

    @Autowired
    private UserReportService userReportService;

    @Test
    public void emailTests(){
//        userReportService.sendReports();
    }
}


