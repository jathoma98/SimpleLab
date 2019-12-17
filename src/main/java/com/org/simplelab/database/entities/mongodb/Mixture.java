package com.org.simplelab.database.entities.mongodb;

import com.org.simplelab.database.entities.sql.Equipment;
import lombok.Data;

@Data
public class Mixture {
    private Equipment equipment;
    private int value;
}
