package com.example.test.controller;

import com.example.test.domain.user.User;
import com.example.test.domain.user.UserDto;
import com.example.test.mapper.UserToDtoMapper;
import com.example.test.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserToDtoMapper userToDtoMapper;
    @Autowired
    private UserService userService;

    private final ObjectMapper objectMapper = getObjectMapper();
    @Test
    void registeredAndReturnedShouldBeEqualsTest() throws Exception {

        User user = getTestUserDto();
        userService.deleteByPhoneNumber(user.getPhoneNumber());

        UserDto userDto = userToDtoMapper.toDto(user);

        String userJson = objectMapper.writeValueAsString(userDto);

        String createdUserJson = mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().is(201))
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserDto userResponse = objectMapper.readValue(createdUserJson, UserDto.class);

        Assertions.assertEquals(user.getName(), userResponse.getName());
        Assertions.assertEquals(user.getEmail(), userResponse.getEmail());

        userService.deleteByPhoneNumber(user.getPhoneNumber());

    }

    private User getTestUserDto() {
        User user = new User();

        user.setPhoneNumber("88888888888");
        user.setPassword("123456");
        user.setName("test");
        user.setEmail("test@test.com");
        user.setPatronymic("test");
        user.setBirthday(LocalDate.of(1990, 1, 1));
        user.setSecondName("test");

        return user;
    }

    private ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        return objectMapper;
    }
}