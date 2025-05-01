package com.example.test.domain.user;

import lombok.Data;

import java.time.LocalDate;

@Data
public class User {

    private Long id;
    private String name;
    private String secondName;
    private String patronymic;

    private LocalDate birthday;
    private String email;
    private String phoneNumber;

    private Role role;
    private String password;


}
