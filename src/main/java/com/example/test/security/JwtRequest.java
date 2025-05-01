package com.example.test.security;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class JwtRequest {

    @NotNull(message = "Phone number should be not null.")
    private String phoneNumber;

    @NotNull(message = "Password should be not null.")
    private String password;
}
