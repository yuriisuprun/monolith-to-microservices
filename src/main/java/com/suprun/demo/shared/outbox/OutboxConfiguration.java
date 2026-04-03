package com.suprun.demo.shared.outbox;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@EnableConfigurationProperties(OutboxProperties.class)
@ConditionalOnProperty(prefix = "app.outbox.publisher", name = "enabled", havingValue = "true")
public class OutboxConfiguration {

    @Bean
    OutboxPublisher outboxPublisher(OutboxEventJpaRepository repo, OutboxProperties props) {
        return new OutboxPublisher(repo, props);
    }
}

