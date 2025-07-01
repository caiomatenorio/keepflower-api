package com.keepflower.api.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.session")
@Data
public class SessionProperties {
    private int expirationSeconds;
}
