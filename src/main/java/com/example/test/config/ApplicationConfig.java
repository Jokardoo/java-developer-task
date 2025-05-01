package com.example.test.config;

import com.example.test.security.JwtTokenFilter;
import com.example.test.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class ApplicationConfig {

    private final JwtTokenProvider tokenProvider;
    private final ApplicationContext applicationContext;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)   // отключаем csrf
                .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configure(httpSecurity)) // включаем cors

                .formLogin(AbstractHttpConfigurer::disable) // отключаем базовую аутентификацию
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // устанавливаем такую конфигурацию сессия, при которой мы не будем хранить состояние

                .exceptionHandling(exceptionHandlingConfigurer -> {

                    exceptionHandlingConfigurer.authenticationEntryPoint((request, response, authException) -> {
                        response.setStatus(HttpStatus.UNAUTHORIZED.value());
                        response.getWriter().write("Unauthorized");
                    });

                    exceptionHandlingConfigurer.accessDeniedHandler((request, response, accessDeniedException) -> {
                        response.setStatus(HttpStatus.FORBIDDEN.value());
                        response.getWriter().write("Forbidden");
                    });
                }) // Если выбрасывается исключение, что пользователь не авторизован, то мы его перехватываем, ставим статус и пишем сообщение unauthorized
                .authorizeHttpRequests(authorizehttpRequest -> {
                    authorizehttpRequest
                            .requestMatchers("/api/auth/**").permitAll() // даем доступ всем

                            .anyRequest().authenticated();   // остальные запросы только для аутентифицированных пользователей
                })
                .anonymous(AbstractHttpConfigurer::disable) // запрещаем анонимное подключение

                .addFilterBefore(new JwtTokenFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class); // описываем авторизацию на токенах

        return httpSecurity.build();

    }

}
