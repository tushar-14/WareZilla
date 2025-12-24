package com.app.warezilla.repository;

import com.app.warezilla.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUserName(String userName);

    void deleteByUserName(String userName);

    List<User> findAllByEmailIsNotNullAndReportEnabledTrue();
}
