package com.javaquasar.cache.conf;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;

@Data
@Component
@ConfigurationProperties(prefix = "app.cache.expiration")
public class CacheExpirationProperties {

    private Integer value = 1;
    private ChronoUnit unit = ChronoUnit.MINUTES;

}
