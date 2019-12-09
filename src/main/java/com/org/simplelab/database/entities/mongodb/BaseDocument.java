package com.org.simplelab.database.entities.mongodb;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public abstract class BaseDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

}
