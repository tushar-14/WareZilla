package com.app.warezilla.repository;

import com.app.warezilla.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUserName(String userName);

    void deleteByUserName(String userName);

    @Query("""
        SELECT DISTINCT u
        FROM User u
        JOIN FETCH u.transactionList t
        WHERE u.reportEnabled = true
        AND t.createdAt BETWEEN :start AND :end
    """)
    List<User> findAllByEmailIsNotNullAndReportEnabledTrue(LocalDateTime start, LocalDateTime end);
}
