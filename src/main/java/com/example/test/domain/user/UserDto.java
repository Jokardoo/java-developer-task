package com.example.test.domain.user;

import com.example.test.service.validation.OnCreate;
import com.example.test.service.validation.OnUpdate;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class UserDto {

    @NotBlank(message = "Name should not be empty!", groups = {OnCreate.class, OnUpdate.class})
    private String name;

    @NotBlank(message = "Second name should not be empty!", groups = {OnCreate.class, OnUpdate.class})
    private String secondName;
    private String patronymic;

    @NotNull(message = "Birthday should not be empty!", groups = OnCreate.class)
    @NotBlank(message = "Birthday should not be blank!", groups = OnCreate.class)
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @NotNull(message = "Email should not be empty!", groups = OnCreate.class)
    @NotBlank(message = "Email should not be blank!", groups = OnCreate.class)
    private String email;

    @NotNull(message = "Phone number should not be empty!", groups = OnCreate.class)
    @NotBlank(message = "Phone number should not be empty!", groups = OnCreate.class)
    private String phoneNumber;
//
//    @JsonProperty(access = JsonProperty.Access.)
    @NotNull(message = "Password should not be empty!", groups = OnCreate.class)
    @NotBlank(message = "Password should not be blank!", groups = OnCreate.class)
    private String password;


}
