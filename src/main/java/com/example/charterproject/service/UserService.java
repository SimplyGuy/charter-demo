package com.example.charterproject.service;

import com.example.charterproject.model.Transaction;
import com.example.charterproject.model.User;
import com.example.charterproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepo;

    @Autowired
    TransactionService transactionService;

    public User findUserById(Long userId) {
        Optional<User> optionalUser = userRepo.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return optionalUser.get();
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

    @Transactional
    public User saveUser(User user) {

        user.setRewardPoint(0);
        return userRepo.save(user);
    }

    public int getRewardPoint(double price) {

        int rewardPoint = 0;

        if (price > 100) {
            rewardPoint += Math.floor((price - 100.0) * 2) + 50;
        }
        else if (price > 50) {
            rewardPoint += Math.floor(price - 50.0);
        }
        return rewardPoint;
    }

    @Transactional
    public User updateRewardPoint(User user, Transaction transaction) {

        int newRewardPoint = getRewardPoint(transaction.getTransactionPrice()) + user.getRewardPoint();
        user.setRewardPoint(newRewardPoint);
        return userRepo.save(user);
    }

    public int findUserRewardPointByMonth(Long userId, int year, int month) {

        List<Transaction> transactions = transactionService.findByUserAndTransactionDateBetween(userId, year, month);
        int monthlyRewardPoint = 0;
        for (Transaction transaction: transactions) {
            monthlyRewardPoint += getRewardPoint(transaction.getTransactionPrice());
        }
        return monthlyRewardPoint;
    }
}
