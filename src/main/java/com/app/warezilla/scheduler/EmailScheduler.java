package com.app.warezilla.scheduler;

import com.app.warezilla.service.UserReportService;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EmailScheduler {

    private final UserReportService userReportService;

    public EmailScheduler(UserReportService userReportService) {
        this.userReportService = userReportService;
    }

    @Scheduled(cron = "0 0 9 ? * MON", zone = "Asia/Kolkata")
    @SchedulerLock(
            name = "weeklyReportJob",
            lockAtMostFor = "30m",
            lockAtLeastFor = "5m"
    ) // Every MON at 9 AM
    public void sendEmailReports() {
        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start = end.minusWeeks(1);
        userReportService.sendReports(start, end);
    }
}
