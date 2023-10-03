package com.example.charterproject.service;

import com.example.charterproject.model.Transaction;
import com.example.charterproject.model.User;
import com.example.charterproject.repository.TransactionRepository;
import com.example.charterproject.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TransactionServiceTest {

    @MockBean
    private UserService userService;

    @Autowired
    private TransactionService transactionService;

    private User testUser;
    private Transaction testTransaction1;

    @BeforeEach
    void setup() {

        testUser = new User();
        testUser.setRewardPoint(0);
        testUser.setUsername("Watson");

        Calendar calendar = Calendar.getInstance();
        calendar.set(2023, 2, 8);
        Date date1 = calendar.getTime();

        testTransaction1 = new Transaction();
        testTransaction1.setTransactionDate(date1);
        testTransaction1.setTransactionInfo("transaction 1");
        testTransaction1.setTransactionPrice(89.00);
    }

    @Test
    void whenSaveTransaction_thenUpdateUserRewardPoints() {

        testTransaction1.setUser(testUser);
        userService.saveUser(testUser);

        transactionService.saveTransaction(testTransaction1);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<Transaction> transactionArgumentCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(userService, times(1)).updateRewardPoint(userCaptor.capture(), transactionArgumentCaptor.capture());
    }
}
