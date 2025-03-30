package com.javaquasar.cache.service;

import com.javaquasar.cache.model.CacheEntry;
import com.javaquasar.cache.model.SaveCacheEntry;
import com.javaquasar.cache.repository.CacheRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class CacheService {

    @Value("${app.cache.expiration.value:1}")
    private Integer expirationValue;
    @Value("${app.cache.expiration.unit:MINUTES}")
    private ChronoUnit expirationUnit;

    private final CacheRepository cacheRepository;

    @Autowired
    public CacheService(CacheRepository cacheRepository) {
        this.cacheRepository = cacheRepository;
    }

    public CacheEntry getCacheEntry(String key) {
        return cacheRepository.findByKey(key);
    }

    public Long save(SaveCacheEntry saveCacheEntry) {
        CacheEntry entry = new CacheEntry();
        entry.setKey(saveCacheEntry.getKey());
        entry.setValue(saveCacheEntry.getValue());
        entry.setCreatedAt(new Date());
        cacheRepository.save(entry);
        return entry.getId();
    }

    @Transactional
    public void deleteExpiredUsingJpa() {
        Date expirationDate = getExpirationDate(expirationValue, expirationUnit);
        cacheRepository.deleteByCreatedAtBefore(expirationDate);
    }

    @Transactional
    public void deleteExpiredUsingJpql() {
        Date expirationDate = getExpirationDate(expirationValue, expirationUnit);
        cacheRepository.deleteOlderThan(expirationDate);
    }

    @Transactional
    public void deleteExpiredUsingNativeQuery() {
        Date expirationDate = getExpirationDate(expirationValue, expirationUnit);
        cacheRepository.deleteOlderThanNativeQuery(expirationDate);
    }

    public Date getExpirationDate(Integer expirationValue, ChronoUnit expirationUnit) {
        Instant now = Instant.now();
        Instant expirationThreshold = now.minus(expirationValue, expirationUnit);
        Date expirationDate = Date.from(expirationThreshold);
        return expirationDate;
    }

}
