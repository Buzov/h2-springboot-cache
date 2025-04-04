package com.javaquasar.cache.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SaveCacheEntry {

    private String key;
    private String value;

}
