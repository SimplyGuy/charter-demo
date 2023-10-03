package com.example.charterproject.controller;

import com.example.charterproject.model.Transaction;
import com.example.charterproject.model.User;
import com.example.charterproject.service.TransactionService;
import com.example.charterproject.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

    @MockBean
    private TransactionService transactionService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;
    private Transaction testTransaction1;
    private Transaction testTransaction2;

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
        testTransaction1.setUser(testUser);
        testTransaction1.setTransactionInfo("transaction 1");
        testTransaction1.setTransactionPrice(89.00);

        calendar.set(2023, 2, 20);
        Date date2 = calendar.getTime();

        testTransaction2 = new Transaction();
        testTransaction2.setTransactionDate(date2);
        testTransaction2.setUser(testUser);
        testTransaction2.setTransactionInfo("transaction 2");
        testTransaction2.setTransactionPrice(49.00);
    }

    @Test
    public void testCreateTransaction() throws Exception {

        when(transactionService.saveTransaction(any(Transaction.class))).thenReturn(testTransaction1);

        mockMvc.perform(post("/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(testTransaction1)))
                .andExpect(status().isOk());

        verify(transactionService, times(1)).saveTransaction(any(Transaction.class));
    }

    @Test
    public void testCreateTransactions() throws Exception {
        List<Transaction> transactions = new ArrayList<>(Arrays.asList(testTransaction1, testTransaction2));

        when(transactionService.saveTransactions(anyList())).thenReturn(transactions);

        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(transactions)))
                .andExpect(status().isOk());

        verify(transactionService, times(1)).saveTransactions(anyList());
    }
}
