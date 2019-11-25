package com.org.simplelab.database;

import com.org.simplelab.database.entities.BaseTable;

public abstract class DBService<T extends BaseTable> {

    public abstract boolean insert(T toInsert);

}
