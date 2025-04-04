package com.javaquasar.cache.controller;

import com.javaquasar.cache.dto.CacheEntryResponse;
import com.javaquasar.cache.model.CacheEntry;
import com.javaquasar.cache.dto.SaveCacheEntry;
import com.javaquasar.cache.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cache")
public class CacheController {

    private final CacheService cacheService;

    @Autowired
    public CacheController(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @GetMapping("/{key}")
    public CacheEntryResponse getCache(@PathVariable String key) {
        var response = cacheService.getCacheEntry(key);
        return response;
    }

    @PostMapping("/")
    public void message(@RequestBody SaveCacheEntry saveCacheEntry) {
        cacheService.save(saveCacheEntry);
    }
}
