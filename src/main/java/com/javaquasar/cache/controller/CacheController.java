package com.javaquasar.cache.controller;

import com.javaquasar.cache.api.VersionProvider;
import com.javaquasar.cache.api.Version;
import com.javaquasar.cache.dto.ICacheEntry;
import com.javaquasar.cache.dto.SaveCacheEntry;
import com.javaquasar.cache.service.CacheService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/cache")
@AllArgsConstructor
public class CacheController {

    private final VersionProvider versionProvider;
    private final CacheService cacheService;

    @GetMapping("/{key}")
    public ICacheEntry getCache(
        @Parameter(
            name = "Accept",
            description = "Specifies the response format and version. Defaults to v1 (application/vnd.javaquasar.v1+json) if omitted.",
            required = false
        )
        @RequestHeader(value = "Accept", defaultValue = "application/vnd.javaquasar.v1+json")
        String acceptHeader,

        @PathVariable
        String key) {

        log.info("Accept: {}", acceptHeader);
        Version version = versionProvider.identifyVersion(acceptHeader);
        var response = cacheService.getCacheEntry(version, key);
        return response;
    }

    @PostMapping("/")
    public void message(@RequestBody SaveCacheEntry saveCacheEntry) {
        cacheService.save(saveCacheEntry);
    }
}
