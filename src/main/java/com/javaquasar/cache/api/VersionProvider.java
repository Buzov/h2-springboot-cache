package com.javaquasar.cache.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.javaquasar.cache.api.Version.*;


@Component
@Slf4j
public class VersionProvider {

    public Version identifyVersion(String version) {
        log.info("version: {}", version);
        if ("application/vnd.javaquasar.v2+json".equalsIgnoreCase(version.trim())) {
            log.info("Request is identified as V2");
            return V2;
        }
        log.info("Request is identified as V1");
        return V1;
    }

}
