package com.example.test.controller;

import com.example.test.domain.user.User;
import com.example.test.domain.user.UserDto;
import com.example.test.mapper.UserToDtoMapper;
import com.example.test.security.JwtRequest;
import com.example.test.security.JwtResponse;
import com.example.test.service.AuthService;
import com.example.test.service.UserService;
import com.example.test.service.validation.OnCreate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpResponse;

@RestController()
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final UserService userService;
    private final UserToDtoMapper userToDtoMapper;
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@Validated(OnCreate.class) @RequestBody UserDto userDto) {
        User user = userToDtoMapper.toModel(userDto);
        User createdUser = userService.create(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(userToDtoMapper.toDto(createdUser));
    }

    @PostMapping("/login")
    public JwtResponse login(@RequestBody @Validated JwtRequest jwtRequest) {
        return authService.login(jwtRequest);
    }

}
