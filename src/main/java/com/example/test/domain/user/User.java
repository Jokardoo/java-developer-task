package com.example.test.domain.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
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

    public User(Long id, String name, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }
}
