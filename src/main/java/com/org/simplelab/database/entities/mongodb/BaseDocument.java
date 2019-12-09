package com.org.simplelab.database.entities.mongodb;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
public abstract class BaseDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    private String _metadata;

}
