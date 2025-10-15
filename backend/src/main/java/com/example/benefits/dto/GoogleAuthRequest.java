package com.example.benefits.dto;

import lombok.Data;

@Data
public class GoogleAuthRequest {
    private String code;
}


// deserialize the request body from fetch to this class