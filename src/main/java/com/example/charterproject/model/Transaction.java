package com.example.charterproject.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name="transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transactionId", unique = true, nullable = false)
    private long transactionId;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "user_Id", nullable = false)
    private User user;

    @Column(name = "transactionInfo", nullable = false, length = 150)
    private String transactionInfo;

    @Column(name = "transactionPrice", nullable = false)
    private double transactionPrice;

    @Temporal(TemporalType.DATE)
    @Column(name = "transactionDate", nullable = false)
    private Date transactionDate;
}
