package com.keepflower.api.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.session.refresh-token")
@Data
public class RefreshTokenProperties {
    private int length;
}
