package com.example.test.controller;

import com.example.test.domain.user.User;
import com.example.test.domain.user.UserDto;
import com.example.test.mapper.UserToDtoMapper;
import com.example.test.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    @Autowired
    private UserToDtoMapper userToDtoMapper;
    private final ObjectMapper objectMapper = getObjectMapper();

    @Test
    public void userToUpdateAndUpdatedUserShouldBeEquals() throws Exception {

        UserDto userDto = userToDtoMapper.toDto(getTestUserDto());
        String userJson = objectMapper.writeValueAsString(userDto);

        userService.deleteByPhoneNumber(userDto.getPhoneNumber());

        String createdUserJson = mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON).content(userJson)).andExpect(status().is(201))
                .andReturn().getResponse().getContentAsString();

        UserDto updatedUserDto = objectMapper.readValue(createdUserJson, UserDto.class);
        assertEquals(userDto.getPhoneNumber(), updatedUserDto.getPhoneNumber());

        User savedUser = userService.getByPhoneNumber(updatedUserDto.getPhoneNumber());

        User userWithUpdatedFields = new User();
        userWithUpdatedFields.setPassword("");

        UserDto userDtoWithUpdatedFields = userToDtoMapper.toDto(userWithUpdatedFields);
        String userToUpdateJson = objectMapper.writeValueAsString(userDtoWithUpdatedFields);


        mockMvc.perform(patch("/api/users/update/" + savedUser.getId())
                        .contentType(MediaType.APPLICATION_JSON).content(userToUpdateJson)).andExpect(status().is(401));

        userService.deleteByPhoneNumber(userDto.getPhoneNumber());
    }


    private User getTestUserDto() {
        User user = new User();

        user.setPhoneNumber("99999999999");
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