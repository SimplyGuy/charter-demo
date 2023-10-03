package com.example.charterproject.model;

import lombok.Data;
import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId", unique = true, nullable = false)
    private long userId;

    @Column(name = "username", nullable = false, length = 20)
    private String username;

    @Column(name = "rewardPoint", nullable = false)
    private int rewardPoint;

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "user")
    private List<Transaction> transactions;
}
