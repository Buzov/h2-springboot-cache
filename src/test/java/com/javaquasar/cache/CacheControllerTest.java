package com.javaquasar.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaquasar.cache.api.Version;
import com.javaquasar.cache.dto.CacheEntryResponseV1;
import com.javaquasar.cache.dto.CacheEntryResponseV2;
import com.javaquasar.cache.dto.ICacheEntry;
import com.javaquasar.cache.dto.SaveCacheEntry;
import com.javaquasar.cache.service.CacheService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.javaquasar.cache.api.VersionProvider;

@WebMvcTest
@ExtendWith(SpringExtension.class)
class CacheControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CacheService cacheService;

    @MockBean
    private VersionProvider versionProvider;

    @Test
    void shouldReturnCacheEntryForGivenKey() throws Exception {
        String key = "testKey";
        ICacheEntry mockEntry = new CacheEntryResponseV1(key, "testValue", new Date());

        Mockito.when(versionProvider.identifyVersion(Mockito.anyString()))
            .thenReturn(Version.V1);

        Mockito.when(cacheService.getCacheEntry(Version.V1, key))
            .thenReturn(mockEntry);

        mockMvc.perform(get("/cache/{key}", key))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.key").value("testKey"))
            .andExpect(jsonPath("$.value").value("testValue"));
    }

    @Test
    void shouldSaveCacheEntry() throws Exception {
        SaveCacheEntry entry = new SaveCacheEntry("saveKey", "saveKey");

        mockMvc.perform(post("/cache/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(entry)))
            .andExpect(status().isOk());

        Mockito.verify(cacheService, times(1)).save(Mockito.any(SaveCacheEntry.class));
    }

    @Test
    void shouldReturnV1CacheEntryWhenAcceptHeaderIsV1() throws Exception {
        String key = "v1Key";
        ICacheEntry mockEntry = new CacheEntryResponseV1(key, "v1Value", new Date());

        Mockito.when(versionProvider.identifyVersion("application/vnd.javaquasar.v1+json"))
            .thenReturn(Version.V1);
        Mockito.when(cacheService.getCacheEntry(Version.V1, key))
            .thenReturn(mockEntry);

        mockMvc.perform(get("/cache/{key}", key)
                .header("Accept", "application/vnd.javaquasar.v1+json"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/vnd.javaquasar.v1+json"))
            .andExpect(jsonPath("$.key").value("v1Key"))
            .andExpect(jsonPath("$.value").value("v1Value"))
            .andExpect(jsonPath("$.createdAt").exists());
    }

    @Test
    void shouldReturnV2CacheEntryWhenAcceptHeaderIsV2() throws Exception {
        String key = "v2Key";
        ICacheEntry mockEntry = new CacheEntryResponseV2(42L, key, "v2Value", new Date(), new Date());

        Mockito.when(versionProvider.identifyVersion("application/vnd.javaquasar.v2+json"))
            .thenReturn(Version.V2);
        Mockito.when(cacheService.getCacheEntry(Version.V2, key))
            .thenReturn(mockEntry);

        mockMvc.perform(get("/cache/{key}", key)
                .header("Accept", "application/vnd.javaquasar.v2+json"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/vnd.javaquasar.v2+json"))
            .andExpect(jsonPath("$.id").value(42))
            .andExpect(jsonPath("$.key").value("v2Key"))
            .andExpect(jsonPath("$.value").value("v2Value"))
            .andExpect(jsonPath("$.createdAt").exists())
            .andExpect(jsonPath("$.updatedAt").exists());
    }
}
