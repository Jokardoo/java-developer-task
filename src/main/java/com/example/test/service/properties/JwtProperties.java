package com.example.test.service.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "security.jwt")   // по этому префиксу мы будем получать данные из properties.yaml
public class JwtProperties {

    private String secret;
    private Long access;
    private Long refresh;

}
