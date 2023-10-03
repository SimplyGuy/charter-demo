package com.example.charterproject.repository;


import com.example.charterproject.model.Transaction;
import com.example.charterproject.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@TestPropertySource(locations="classpath:application-test.properties")
@DataJpaTest
public class TransactionRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TransactionRepository transactionRepo;

    @Autowired UserRepository userRepo;

    private User testUser;
    private Transaction testTransaction1;
    private Transaction testTransaction2;
    private Transaction testTransaction3;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setRewardPoint(0);
        testUser.setUsername("Watson");

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

        entityManager.persistAndFlush(testUser);
        entityManager.persistAndFlush(testTransaction1);
        entityManager.persistAndFlush(testTransaction2);
        entityManager.persistAndFlush(testTransaction3);
    }


    @Test
    void whenFindByUser_thenReturnListOfTransactions() {

        List<Transaction> foundTransactions = transactionRepo.findByUser(testUser);

        assertThat(foundTransactions).isNotEmpty();
        assertThat(foundTransactions).hasSize(3);
        assertThat(foundTransactions).contains(testTransaction1, testTransaction2, testTransaction3);
    }

    @Test
    void whenFindByUserAndMonth_thenReturnListOfTransactions() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(2023, 2, 1);
        Date startdate = calendar.getTime();
        calendar.set(2023, 2, 31);
        Date endDate = calendar.getTime();
        List<Transaction> foundTransactions = transactionRepo.findByUserAndTransactionDateBetween(testUser, startdate, endDate);

        assertThat(foundTransactions).isNotEmpty();
        assertThat(foundTransactions).hasSize(2);
        assertThat(foundTransactions).contains(testTransaction1, testTransaction2);
    }
}
