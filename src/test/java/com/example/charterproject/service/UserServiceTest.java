package com.example.charterproject.service;

import com.example.charterproject.model.Transaction;
import com.example.charterproject.model.User;
import com.example.charterproject.repository.TransactionRepository;
import com.example.charterproject.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    @MockBean
    private UserRepository userRepo;

    @MockBean
    private TransactionRepository transactionRepo;

    @Autowired
    private UserService userService;

    @MockBean
    private TransactionService transactionService;

    private User testUser;
    private User updateUser;
    private User updateUser2;
    private User updateUser3;
    private Transaction testTransaction1;
    private Transaction testTransaction2;
    private Transaction testTransaction3;

    @BeforeEach
    void setup() {

        testUser = new User();
        testUser.setRewardPoint(0);
        testUser.setUsername("Watson");

        updateUser = new User();
        updateUser.setUserId(testUser.getUserId());
        updateUser.setRewardPoint(39);
        updateUser.setUsername("Watson");

        updateUser2 = new User();
        updateUser2.setUserId(testUser.getUserId());
        updateUser2.setRewardPoint(39);
        updateUser2.setUsername("Watson");

        updateUser3 = new User();
        updateUser3.setUserId(testUser.getUserId());
        updateUser3.setRewardPoint(129);
        updateUser3.setUsername("Watson");

        Calendar calendar = Calendar.getInstance();
        calendar.set(2023, 2, 8);
        Date date1 = calendar.getTime();
        calendar.set(2023, 2, 20);
        Date date2 = calendar.getTime();
        calendar.set(2023, 3, 20);
        Date date3 = calendar.getTime();

        testTransaction1 = new Transaction();
        testTransaction1.setTransactionDate(date1);
        testTransaction1.setUser(testUser);
        testTransaction1.setTransactionInfo("transaction 1");
        testTransaction1.setTransactionPrice(89.00);

        testTransaction2 = new Transaction();
        testTransaction2.setTransactionDate(date2);
        testTransaction2.setUser(testUser);
        testTransaction2.setTransactionInfo("transaction 2");
        testTransaction2.setTransactionPrice(49.00);

        testTransaction3 = new Transaction();
        testTransaction3.setTransactionDate(date3);
        testTransaction3.setUser(testUser);
        testTransaction3.setTransactionInfo("transaction 3");
        testTransaction3.setTransactionPrice(120.00);

        when(userRepo.save(testUser)).thenReturn(testUser);
        when(userRepo.findById(testUser.getUserId())).thenReturn(Optional.of(testUser));
    }

    @Test
    void testUpdateUserRewardPoints() {

        User actualUser = userService.updateRewardPoint(testUser, testTransaction1);

        assertThat(actualUser.getRewardPoint()).isEqualTo(updateUser.getRewardPoint());

        User actualUser2 = userService.updateRewardPoint(testUser, testTransaction2);
        assertThat(actualUser2.getRewardPoint()).isEqualTo(updateUser2.getRewardPoint());

        User actualUser3 = userService.updateRewardPoint(testUser, testTransaction3);
        assertThat(actualUser3.getRewardPoint()).isEqualTo(updateUser3.getRewardPoint());
    }

    @Test
    void testFindUserRewardPointByMonth() {

        int year = 2023;
        int month = 2;

        userService.updateRewardPoint(testUser, testTransaction1);
        userService.updateRewardPoint(testUser, testTransaction2);
        userService.updateRewardPoint(testUser, testTransaction3);

        List<Transaction> transactions = Arrays.asList(testTransaction1, testTransaction2);

        when(transactionService.findByUserAndTransactionDateBetween(testUser.getUserId(), year, month)).thenReturn(transactions);

        int actualRewardPoint = userService.findUserRewardPointByMonth(testUser.getUserId(), year, month);
        assertThat(actualRewardPoint).isEqualTo(updateUser2.getRewardPoint());
    }
}
