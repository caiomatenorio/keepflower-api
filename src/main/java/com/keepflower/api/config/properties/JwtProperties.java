package com.keepflower.api.config.properties;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component
@ConfigurationProperties(prefix = "app.jwt")
public class JwtProperties {
    private int expirationSeconds;
    private String secret;
}
