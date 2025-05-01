package com.example.test.security;

import com.example.test.domain.user.Role;
import com.example.test.domain.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;

public class JwtEntityFactory {

    public static JwtEntity create(User user) {
        return new JwtEntity(user.getId(),
                user.getPhoneNumber(),  // Вместо username будем использовать номер телефона, т.к. он уникален
                user.getName(),
                user.getPassword(),
                mapToGrantedAuthorities(user.getRole()));
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(Role role) {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.name());
        return Collections.singletonList(authority);
    }

}
