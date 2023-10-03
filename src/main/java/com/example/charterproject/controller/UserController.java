package com.example.charterproject.controller;

import com.example.charterproject.model.User;
import com.example.charterproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/users/register")
    public ResponseEntity<Void> addUser(@RequestBody User user) {
        userService.saveUser(user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<User> findUserById(@PathVariable("userId") long userId) {
        User user = userService.findUserById(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/users/rewardPoint/{userId}")
    public ResponseEntity<Integer> findUserTotalRewardPoint(@PathVariable("userId") long userId) {
        User user = userService.findUserById(userId);
        return new ResponseEntity<>(user.getRewardPoint(), HttpStatus.OK);
    }

    @GetMapping("/users/rewardPointByMonth/{userId}")
    public ResponseEntity<Integer> findUserMonthlyRewardPoint(@PathVariable("userId") long userId, int year, int month) {
        int rewardPoint = userService.findUserRewardPointByMonth(userId, year, month);
        return new ResponseEntity<>(rewardPoint, HttpStatus.OK);
    }
}
