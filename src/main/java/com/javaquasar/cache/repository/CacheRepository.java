package com.javaquasar.cache.repository;

import com.javaquasar.cache.model.CacheEntry;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface CacheRepository extends CrudRepository<CacheEntry, Long> {

    CacheEntry findByKey(String key);

    // JPA
    void deleteByCreatedAtBefore(Date expirationDate);

    // JPQL
    @Modifying
    @Query("DELETE FROM CacheEntry c WHERE c.createdAt < :expirationDate")
    void deleteOlderThan(@Param("expirationDate") Date expirationDate);

    // Native Query
    @Modifying
    @Query(value = "DELETE FROM cache WHERE created_at < :expirationDate", nativeQuery = true)
    void deleteOlderThanNativeQuery(@Param("expirationDate") Date expirationDate);

}
