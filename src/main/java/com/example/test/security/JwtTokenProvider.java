package com.example.test.security;

import com.example.test.domain.user.Role;
import com.example.test.domain.user.User;
import com.example.test.exception.AccessDeniedException;
import com.example.test.service.UserService;
import com.example.test.service.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;

    private final UserDetailsService userDetailsService;

    private final UserService userService;

    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }

    public String createAccessToken(Long userId, String phoneNumber, Role role) {

        Claims claims = (Claims) Jwts.claims().setSubject(phoneNumber);
        claims.put("id", userId);
        claims.put("roles", role);

        Date now = new Date();
        Date validity = new Date(now.getTime() + jwtProperties.getAccess());    // настоящее время + время жизни токена

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key)
                .compact();
    }

    public String createRefreshToken(Long userId, String phoneNumber) {
        Claims claims = (Claims) Jwts.claims().setSubject(phoneNumber);
        claims.put("id", userId);

        Date now = new Date();
        Date validity = new Date(now.getTime() + jwtProperties.getRefresh());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key)
                .compact();
    }

    public JwtResponse refreshUserTokens(String refreshToken) {
        JwtResponse jwtResponse = new JwtResponse();

        if (!validateToken(refreshToken)) {
            throw new AccessDeniedException("Refresh token is not valid.");
        }

        Long userId = Long.valueOf(getId(refreshToken));
        User user = userService.getById(userId);

        jwtResponse.setId(userId);
        jwtResponse.setPhoneNumber(user.getPhoneNumber());
        jwtResponse.setAccessToken(createAccessToken(userId, user.getPhoneNumber(), user.getRole()));
        jwtResponse.setRefreshToken(createRefreshToken(userId, user.getPhoneNumber()));

        return jwtResponse;
    }

    public boolean validateToken(String token) {

        return !Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(key.getEncoded()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration().before(new Date());
    }

    private String getId(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getId();
    }

    public Authentication getAuthentication(String token) {
        String username = getUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    private String getUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }


    private List<String> resolveRoles(Set<Role> roles) {
        return roles.stream()
                .map(Enum::name)
                .collect(Collectors.toList());
    }

}
