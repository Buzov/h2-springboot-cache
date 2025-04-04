package com.javaquasar.cache.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Cache request for API, includes basic fields")
public record SaveCacheEntry(@Schema(description = "Entry identifier", example = "1") String key,
                             @Schema(description = "Value of the entry", example = "java") String value) {
}
