package com.suprun.demo.shared.outbox;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.outbox.publisher")
public record OutboxProperties(
        boolean enabled,
        int batchSize
) {
    public OutboxProperties {
        if (batchSize <= 0) {
            batchSize = 50;
        }
    }
}

