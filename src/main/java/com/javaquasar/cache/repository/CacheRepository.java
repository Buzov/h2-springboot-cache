package com.javaquasar.cache.repository;

import com.javaquasar.cache.model.CacheEntry;
import org.springframework.data.repository.CrudRepository;
import java.util.Date;

public interface CacheRepository extends CrudRepository<CacheEntry, Long> {

    CacheEntry findByKey(String key);

    void deleteByCreatedAtBefore(Date expirationDate);

}
