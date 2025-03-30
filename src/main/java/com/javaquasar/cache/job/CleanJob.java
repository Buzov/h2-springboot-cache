package com.javaquasar.cache.job;

import com.javaquasar.cache.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CleanJob {

    private final CacheService cacheService;

    @Autowired
    public CleanJob(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @Scheduled(cron = "${app.cache.expiration.cron:0 * * * * *}")
    public void removeOldRecords() {
        cacheService.deleteExpiredUsingJpql();
    }

}
