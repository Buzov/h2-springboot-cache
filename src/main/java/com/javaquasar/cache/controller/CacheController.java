package com.javaquasar.cache.controller;

import com.javaquasar.cache.api.VersionProvider;
import com.javaquasar.cache.api.Version;
import com.javaquasar.cache.dto.ICacheEntry;
import com.javaquasar.cache.dto.SaveCacheEntry;
import com.javaquasar.cache.service.CacheService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import com.javaquasar.cache.dto.CacheEntryResponseV1;
import com.javaquasar.cache.dto.CacheEntryResponseV2;
import io.swagger.v3.oas.annotations.Operation;

@Slf4j
@RestController
@RequestMapping("/cache")
@AllArgsConstructor
public class CacheController {

    private final VersionProvider versionProvider;
    private final CacheService cacheService;

    @Operation
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Cache entry (version 1)",
            content = {
                @Content(
                    mediaType = "application/vnd.javaquasar.v1+json",
                    schema = @Schema(implementation = CacheEntryResponseV1.class),
                    examples = @ExampleObject(
                        name = "V1 Response Example",
                        value = """
                                    {
                                      "key": "1",
                                      "value": "java",
                                      "createdAt": "2025-04-04T23:00:04.236+00:00"
                                    }
                                """
                    )
                ),
                @Content(
                    mediaType = "application/vnd.javaquasar.v2+json",
                    schema = @Schema(implementation = CacheEntryResponseV2.class),
                    examples = @ExampleObject(
                        name = "V2 Response Example",
                        value = """
                                    {
                                      "id": 1,
                                      "key": "1",
                                      "value": "java",
                                      "createdAt": "2025-04-04T23:00:04.236+00:00"
                                    }
                                """
                    )
                )
            }
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Cache entry not found"
        )
    })
    @GetMapping(
        value = "/{key}",
        produces = {
            "application/vnd.javaquasar.v1+json",
            "application/vnd.javaquasar.v2+json"
        }
    )
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
