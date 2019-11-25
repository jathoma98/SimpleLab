package com.org.simplelab.database.entities;

import lombok.Data;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@MappedSuperclass
public abstract class BaseTable implements Persistable<Long> {

    /**
     * Fields inherited by all entities in the database
     */

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private long id;

    @Column(name = "created_date")
    private String createdDate;

    private long timestamp;

    public String _metadata;

    public BaseTable(){
        if (isNew()) {
            Date date = new Date();
            SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy HH:mm");
            this.createdDate = df.format(date);
            long now = java.time.Instant.now().toEpochMilli();
            this.timestamp = now;
        }
    }

    //immutable fields, cannot be set outside of constructor.
    public void setCreatedDate(String createdDate) {}

    public void setTimestamp(long timestamp) {}


    /**
     * Below are used internally by JPA to check
     * whether an entity is new or already exists
     * in the DB.
     */

    @Override
    public Long getId(){
        return id;
    }

    @Transient
    private boolean isNew = true;

    @Override
    public boolean isNew(){
        return isNew;
    }

    @PrePersist
    @PostLoad
    void markNotNew(){
        this.isNew = false;
    }




}
