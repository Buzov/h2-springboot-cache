package com.javaquasar.cache.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

import static jakarta.persistence.GenerationType.IDENTITY;

@Data
@ToString
@Entity
@Table(name = "cache")
public class CacheEntry {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @Column(name = "cache_key")
    private String key;
    @Column(name = "cache_value")
    private String value;
    private Date createdAt;
}
