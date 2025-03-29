package com.javaquasar.cache.repository;

import com.javaquasar.cache.model.CacheEntry;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface CacheRepository extends CrudRepository<CacheEntry, Long> {

    CacheEntry findByKey(String key);

    void deleteByCreatedAtBefore(Date expirationDate);

    @Modifying
    @Query("DELETE FROM CacheEntry c WHERE c.createdAt < :expirationDate")
    void deleteOlderThan(@Param("expirationDate") Date expirationDate);

}
