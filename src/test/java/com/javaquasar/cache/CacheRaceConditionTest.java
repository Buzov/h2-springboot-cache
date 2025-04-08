package com.javaquasar.cache;

import com.javaquasar.cache.model.CacheEntry;
import com.javaquasar.cache.repository.CacheRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
class CacheRaceConditionTest {

    @Autowired
    private PlatformTransactionManager transactionManager;
    @Autowired
    private CacheRepository cacheRepository;

    private void runInTransaction(Runnable action) {
        TransactionTemplate tx = new TransactionTemplate(transactionManager);
        tx.executeWithoutResult(status -> action.run());
    }

    @Test
    void shouldDemonstrateRaceConditionBetweenSaveAndCleanup() throws Exception {
        // Given: a stale entry that should be cleaned
        CacheEntry entry = new CacheEntry();
        entry.setKey("race-key");
        entry.setValue("old");
        entry.setCreatedAt(Date.from(Instant.now().minus(Duration.ofMinutes(15))));
        entry.setUpdatedAt(Date.from(Instant.now().minus(Duration.ofMinutes(15))));
        entry = cacheRepository.save(entry);
        Long id = entry.getId();

        CountDownLatch latch = new CountDownLatch(1);

        // Thread 1: simulate an update (like saveOrUpdate)
        Thread updateThread = new Thread(() -> {
            try {
                latch.await(); // wait until both threads are ready
                Optional<CacheEntry> optional = cacheRepository.findById(id);
                if (optional.isPresent()) {
                    CacheEntry toUpdate = optional.get();
                    toUpdate.setValue("updated");
                    toUpdate.setUpdatedAt(new Date()); // new timestamp
                    cacheRepository.save(toUpdate);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Thread 2: simulate scheduled cleanup
        Thread deleteThread = new Thread(() -> {
            try {
                latch.await();
                runInTransaction(() -> {
                    Instant expiration = Instant.now().minus(Duration.ofMinutes(10));
                    cacheRepository.deleteOlderThan(Date.from(expiration));
                });
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Start both threads
        updateThread.start();
        deleteThread.start();

        // Release the latch
        latch.countDown();

        // Wait for threads to finish
        updateThread.join();
        deleteThread.join();

        // Then: check if the record still exists
        Optional<CacheEntry> maybeStillThere = cacheRepository.findById(id);

        // ❗️ This is the actual test: it will likely fail unless you fix the race
        assertTrue(maybeStillThere.isPresent(), "Entry should not be deleted if it was just updated");
    }
}
