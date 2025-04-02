package com.javaquasar.cache.job;

import com.javaquasar.cache.service.CacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CleanJob {

    private static final Logger log = LoggerFactory.getLogger(CleanJob.class);

    private final CacheService cacheService;

    @Autowired
    public CleanJob(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @Scheduled(cron = "${app.cache.expiration.cron:0 * * * * *}")
    public void removeOldRecords() {
        log.info("Removing old records");
        cacheService.deleteExpiredUsingJpql();
    }

}
