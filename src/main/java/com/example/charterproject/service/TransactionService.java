package com.example.charterproject.service;

import com.example.charterproject.model.Transaction;
import com.example.charterproject.model.User;
import com.example.charterproject.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    TransactionRepository transactionRepo;

    @Autowired
    UserService userService;

    public List<Transaction> findTransactionsByUserId(Long userId) {
        User user = userService.findUserById(userId);
        return transactionRepo.findByUser(user);
    }

    public List<Transaction> findByUserAndTransactionDateBetween(Long userId, int year, int month) {
        User user = userService.findUserById(userId);
        Calendar calendar = Calendar.getInstance();

        calendar.set(year, month - 1, 1, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date startDate = calendar.getTime();

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        Date endDate = calendar.getTime();

        return transactionRepo.findByUserAndTransactionDateBetween(user, startDate, endDate);
    }

    @Transactional
    public Transaction saveTransaction(Transaction transaction) {

        User user = transaction.getUser();
        userService.updateRewardPoint(user, transaction);
        return transactionRepo.save(transaction);
    }

    @Transactional
    public List<Transaction> saveTransactions(List<Transaction> transactions) {

        for (Transaction transaction: transactions) {
            User user = transaction.getUser();
            userService.updateRewardPoint(user, transaction);
        }
        return transactionRepo.saveAll(transactions);
    }


}
