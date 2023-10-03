package com.example.charterproject.controller;

import com.example.charterproject.model.Transaction;
import com.example.charterproject.model.User;
import com.example.charterproject.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Calendar;
import java.util.Date;

import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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
        testTransaction1.setUser(testUser);
        testTransaction1.setTransactionInfo("transaction 1");
        testTransaction1.setTransactionPrice(89.00);
    }

    @Test
    void testAddUser() throws Exception {

        when(userService.saveUser(any(User.class))).thenReturn(testUser);

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                        .andExpect(status().isOk());

        verify(userService, times(1)).saveUser(any(User.class));
    }

    @Test
    void testfindUserById() throws Exception {

        Long userId = 1L;
        testUser.setUserId(userId);
        when(userService.findUserById(userId)).thenReturn(testUser);

        mockMvc.perform(get("/users/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId", is(userId.intValue())));
    }

    @Test
    void testFindUserTotalRewardPoint() throws Exception {
        Long userId = 1L;
        testUser.setUserId(userId);
        testUser.setRewardPoint(100);

        when(userService.findUserById(userId)).thenReturn(testUser);

        mockMvc.perform(get("/users/rewardPoint/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(100)));
    }

    @Test
    void testFindUserMonthlyRewardPoint() throws Exception {
        Long userId = 1L;
        int year = 2023;
        int month = 3;
        int rewardPoint = 200;

        when(userService.findUserRewardPointByMonth(userId, year, month)).thenReturn(rewardPoint);

        mockMvc.perform(get("/users/rewardPointByMonth/{userId}", userId)
                        .param("year", String.valueOf(year))
                        .param("month", String.valueOf(month)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(rewardPoint)));
    }
}
