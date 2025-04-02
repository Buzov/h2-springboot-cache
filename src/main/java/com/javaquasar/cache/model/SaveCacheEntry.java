package com.javaquasar.cache.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SaveCacheEntry {

    private String key;
    private String value;

}
