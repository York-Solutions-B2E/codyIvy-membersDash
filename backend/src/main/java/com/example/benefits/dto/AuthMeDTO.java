package com.example.benefits.dto;

import java.util.UUID;
import lombok.*;

@Getter
@Setter
public class AuthMeDTO {
    private UUID userId;
    private String email;
    private String name;
}
