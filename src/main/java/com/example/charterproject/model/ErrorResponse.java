package com.example.charterproject.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {

    private int errorCode;
    private String errorMsg;
}
