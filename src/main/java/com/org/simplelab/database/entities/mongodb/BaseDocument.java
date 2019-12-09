package com.org.simplelab.database.entities.mongodb;

import lombok.Data;
import org.springframework.data.domain.Persistable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
public abstract class BaseDocument{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    private long timestamp;

    private long lastUpdated;

    BaseDocument(){
        this.timestamp = java.time.Instant.now().toEpochMilli();
        this.lastUpdated = timestamp;
    }

    public void setTimestamp(long timestamp){ }

    private String _metadata;

}
