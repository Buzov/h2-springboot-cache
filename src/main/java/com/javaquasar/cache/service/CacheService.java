package com.javaquasar.cache.service;

import com.javaquasar.cache.conf.CacheExpirationProperties;
import com.javaquasar.cache.api.Version;
import com.javaquasar.cache.dto.CacheEntryResponseV1;
import com.javaquasar.cache.dto.CacheEntryResponseV2;
import com.javaquasar.cache.dto.ICacheEntry;
import com.javaquasar.cache.model.CacheEntry;
import com.javaquasar.cache.dto.SaveCacheEntry;
import com.javaquasar.cache.repository.CacheRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
@AllArgsConstructor
public class CacheService {

    private final CacheExpirationProperties cacheExpirationProperties;
    private final CacheRepository cacheRepository;

    public ICacheEntry getCacheEntry(Version version, String key) {
        CacheEntry cacheEntry = cacheRepository.findByKey(key);
        log.info("cacheEntry: {}", cacheEntry);
        switch (version) {
            case V1 -> {
                return new CacheEntryResponseV1(
                    cacheEntry.getKey(),
                    cacheEntry.getValue(),
                    cacheEntry.getCreatedAt()
                );
            }
            case V2 -> {
                return new CacheEntryResponseV2(
                    cacheEntry.getId(),
                    cacheEntry.getKey(),
                    cacheEntry.getValue(),
                    cacheEntry.getCreatedAt()
                );
            }
        }

        return new ICacheEntry() {};
    }

    public Long save(SaveCacheEntry saveCacheEntry) {
        log.info("Save cache entry: {}", saveCacheEntry);
        CacheEntry entry = new CacheEntry();
        entry.setKey(saveCacheEntry.key());
        entry.setValue(saveCacheEntry.value());
        entry.setCreatedAt(new Date());
        cacheRepository.save(entry);
        return entry.getId();
    }

    @Transactional
    public void deleteExpiredUsingJpa() {
        Date expirationDate = getExpirationDate();
        cacheRepository.deleteByCreatedAtBefore(expirationDate);
    }

    @Transactional
    public void deleteExpiredUsingJpql() {
        log.info("Delete expired cache entry Jpql");
        Date expirationDate = getExpirationDate();
        cacheRepository.deleteOlderThan(expirationDate);
    }

    @Transactional
    public void deleteExpiredUsingNativeQuery() {
        Date expirationDate = getExpirationDate();
        cacheRepository.deleteOlderThanNativeQuery(expirationDate);
    }

    public Date getExpirationDate() {
        return getExpirationDate(
            cacheExpirationProperties.getValue(),
            cacheExpirationProperties.getUnit()
        );
    }

    public Date getExpirationDate(Integer expirationValue, ChronoUnit expirationUnit) {
        Instant now = Instant.now();
        Instant expirationThreshold = now.minus(expirationValue, expirationUnit);
        Date expirationDate = Date.from(expirationThreshold);
        log.info("expirationDate: {}", expirationDate);
        return expirationDate;
    }

}
