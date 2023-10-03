package com.example.charterproject.controller;

import com.example.charterproject.model.Transaction;
import com.example.charterproject.model.User;
import com.example.charterproject.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @PostMapping("/transaction")
    public ResponseEntity<Void> createTransaction(@RequestBody Transaction transaction) {
        transactionService.saveTransaction(transaction);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/transactions")
    public ResponseEntity<Void> createTransactions(@RequestBody List<Transaction> transactions) {
        transactionService.saveTransactions(transactions);
        return ResponseEntity.ok().build();
    }
}
