package com.example.charterproject.repository;

import com.example.charterproject.model.Transaction;
import com.example.charterproject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByUser(User user);

    List<Transaction> findByUserAndTransactionDateBetween(User user, Date startDate, Date endDate);
}
