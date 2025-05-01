package com.example.test.service;

import com.example.test.domain.user.User;
import com.example.test.security.JwtRequest;
import com.example.test.security.JwtResponse;
import com.example.test.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public JwtResponse login(JwtRequest jwtRequest) {

        JwtResponse jwtResponse = new JwtResponse();

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getPhoneNumber(), jwtRequest.getPassword()));

        User user =  userService.getByPhoneNumber(jwtRequest.getPhoneNumber());

        jwtResponse.setId(user.getId());
        jwtResponse.setPhoneNumber(user.getPhoneNumber());
        jwtResponse.setAccessToken(jwtTokenProvider.createAccessToken(user.getId(), user.getPhoneNumber(), user.getRole()));
        jwtResponse.setRefreshToken(jwtTokenProvider.createRefreshToken(user.getId(), user.getPhoneNumber()));

        return jwtResponse;
    }

}
