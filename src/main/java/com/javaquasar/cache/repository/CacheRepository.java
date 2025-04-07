package com.javaquasar.cache.repository;

import com.javaquasar.cache.model.CacheEntry;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.Optional;

public interface CacheRepository extends CrudRepository<CacheEntry, Long> {

    Optional<CacheEntry> findByKey(String key);

    // JPA
    void deleteByUpdatedAtBefore(Date expirationDate);

    // JPQL
    @Modifying
    @Query("DELETE FROM CacheEntry c WHERE c.updatedAt < :expirationDate")
    void deleteOlderThan(@Param("expirationDate") Date expirationDate);

    // Native Query
    @Modifying
    @Query(value = "DELETE FROM cache WHERE updated_at < :expirationDate", nativeQuery = true)
    void deleteOlderThanNativeQuery(@Param("expirationDate") Date expirationDate);

}
