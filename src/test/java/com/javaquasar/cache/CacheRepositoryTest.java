package com.javaquasar.cache;

import com.javaquasar.cache.model.CacheEntry;
import com.javaquasar.cache.repository.CacheRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CacheRepositoryTest {

    @Autowired
    private CacheRepository cacheRepository;

    @Test
    void shouldFindByKey() {
        CacheEntry entry = new CacheEntry();
        entry.setKey("myKey");
        entry.setValue("myValue");
        entry.setCreatedAt(new Date());

        cacheRepository.save(entry);

        CacheEntry found = cacheRepository.findByKey("myKey");
        assertThat(found).isNotNull();
        assertThat(found.getValue()).isEqualTo("myValue");
    }

    @Test
    void shouldDeleteByCreatedAtBefore() {
        Date now = new Date();

        CacheEntry oldEntry = new CacheEntry();
        oldEntry.setKey("old");
        oldEntry.setValue("oldValue");
        oldEntry.setCreatedAt(new Date(now.getTime() - 100000)); // 100 sec ago

        CacheEntry recentEntry = new CacheEntry();
        recentEntry.setKey("recent");
        recentEntry.setValue("recentValue");
        recentEntry.setCreatedAt(new Date(now.getTime() + 100000)); // 100 sec in future

        cacheRepository.save(oldEntry);
        cacheRepository.save(recentEntry);

        cacheRepository.deleteByCreatedAtBefore(now);

        List<CacheEntry> remaining = (List<CacheEntry>) cacheRepository.findAll();
        assertThat(remaining).hasSize(1);
        assertThat(remaining.get(0).getKey()).isEqualTo("recent");
    }

    @Test
    void shouldDeleteUsingJPQL() {
        Date now = new Date();

        CacheEntry oldEntry = new CacheEntry();
        oldEntry.setKey("jpqlOld");
        oldEntry.setValue("value");
        oldEntry.setCreatedAt(new Date(now.getTime() - 200000));

        cacheRepository.save(oldEntry);

        cacheRepository.deleteOlderThan(now);

        assertThat(cacheRepository.findByKey("jpqlOld")).isNull();
    }

    @Test
    void shouldDeleteUsingNativeQuery() {
        Date now = new Date();

        CacheEntry oldEntry = new CacheEntry();
        oldEntry.setKey("nativeOld");
        oldEntry.setValue("value");
        oldEntry.setCreatedAt(new Date(now.getTime() - 300000));

        cacheRepository.save(oldEntry);

        cacheRepository.deleteOlderThanNativeQuery(now);

        assertThat(cacheRepository.findByKey("nativeOld")).isNull();
    }
}
