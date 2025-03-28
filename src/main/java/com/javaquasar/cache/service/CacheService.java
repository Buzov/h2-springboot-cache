package com.javaquasar.cache.service;

import com.javaquasar.cache.model.CacheEntry;
import com.javaquasar.cache.model.SaveCacheEntry;
import com.javaquasar.cache.repository.CacheRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Service
public class CacheService {

    private final CacheRepository cacheRepository;

    @Autowired
    public CacheService(CacheRepository cacheRepository) {
        this.cacheRepository = cacheRepository;
    }

    public CacheEntry getCacheEntry(String key) {
        return cacheRepository.findByKey(key);
    }

    public void save(SaveCacheEntry saveCacheEntry) {
        CacheEntry entry = new CacheEntry();
        entry.setKey(saveCacheEntry.getKey());
        entry.setValue(saveCacheEntry.getValue());
        entry.setCreatedAt(new Date(new java.util.Date().getTime()));
        cacheRepository.save(entry);
    }





}
