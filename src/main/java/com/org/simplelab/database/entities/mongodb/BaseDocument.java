package com.org.simplelab.database.entities.mongodb;

import lombok.Data;
import org.springframework.data.domain.Persistable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
public abstract class BaseDocument{
    protected static final String NOT_FOUND_KEY = "N/A";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String _id;

    private long timestamp;

    private long lastUpdated;

    BaseDocument(){
        this.timestamp = java.time.Instant.now().toEpochMilli();
        this.lastUpdated = timestamp;
    }

    public boolean exists(){
        return !(get_id().equals(NOT_FOUND_KEY));
    }

    public void setTimestamp(long timestamp){ }

    private String _metadata;

}
