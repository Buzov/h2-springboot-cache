package com.javaquasar.cache.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

@Schema(description = "Represents a cached key-value pair")
public record CacheEntryResponseV1(

    @Schema(description = "Key used to store the value", example = "1")
    String key,

    @Schema(description = "The value stored in the cache", example = "java")
    String value,

    @Schema(description = "Timestamp when the entry was created", example = "2025-04-04T23:00:04.236+00:00")
    Date createdAt

) implements ICacheEntry
{}
