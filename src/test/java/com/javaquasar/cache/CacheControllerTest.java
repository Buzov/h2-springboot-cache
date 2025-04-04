package com.javaquasar.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaquasar.cache.model.CacheEntry;
import com.javaquasar.cache.model.SaveCacheEntry;
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

import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@ExtendWith(SpringExtension.class)
class CacheControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CacheService cacheService;

    @Test
    void shouldReturnCacheEntryForGivenKey() throws Exception {
        String key = "testKey";
        CacheEntry mockEntry = new CacheEntry();
        mockEntry.setKey(key);
        mockEntry.setValue("testValue");

        Mockito.when(cacheService.getCacheEntry(key)).thenReturn(mockEntry);

        mockMvc.perform(get("/cache/{key}", key))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.key").value("testKey"))
            .andExpect(jsonPath("$.value").value("testValue"));
    }

    @Test
    void shouldSaveCacheEntry() throws Exception {
        SaveCacheEntry entry = new SaveCacheEntry();
        entry.setKey("saveKey");
        entry.setValue("saveValue");

        mockMvc.perform(post("/cache/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(entry)))
            .andExpect(status().isOk());

        Mockito.verify(cacheService, times(1)).save(Mockito.any(SaveCacheEntry.class));
    }
}
