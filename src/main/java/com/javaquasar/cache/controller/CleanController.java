package com.javaquasar.cache.controller;

import com.javaquasar.cache.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clean")
public class CleanController {

    private final CacheService cacheService;

    @Autowired
    public CleanController(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @PostMapping("/")
    public void message(@RequestParam DeleteType type) {
        switch (type) {
            case JPA -> {
                cacheService.deleteExpiredUsingJpa();
                break;
            }
            case JPQL -> {
                cacheService.deleteExpiredUsingJpql();
                break;
            }
            case NATIVE_QUERY -> {
                cacheService.deleteExpiredUsingNativeQuery();
                break;
            }
        }
    }

    public enum DeleteType {
        JPA,
        JPQL,
        NATIVE_QUERY
    }

}
