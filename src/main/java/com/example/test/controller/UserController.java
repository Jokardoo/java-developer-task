package com.example.test.controller;

import com.example.test.domain.user.User;
import com.example.test.domain.user.UserDto;
import com.example.test.domain.user.UserShortInfo;
import com.example.test.mapper.UserToDtoMapper;
import com.example.test.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;
    private final UserToDtoMapper userToDtoMapper;

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable(name = "id") Long id) {
        return userToDtoMapper.toDto(userService.getById(id));
    }

    @GetMapping("/contact-info/{id}")
    public UserShortInfo getUserContactInfo(@PathVariable(name = "id") Long id) {
        return userToDtoMapper.userToShortInfo(userService.getById(id));
    }

    @PostMapping("/update/{id}")
    public UserDto updateUser(@PathVariable(name = "id") Long id, @RequestBody @Validated UserDto userDto) {

        User updatedUser = userService.update(id, userToDtoMapper.toModel(userDto));
        return userToDtoMapper.toDto(updatedUser);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable(name = "id") Long id) {
        userService.deleteById(id);
    }
}
