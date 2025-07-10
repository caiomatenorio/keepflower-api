package com.keepflower.api.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.session.jwt")
@Data
public class JwtProperties {
    private int expirationSeconds;
    private String secret;
}
