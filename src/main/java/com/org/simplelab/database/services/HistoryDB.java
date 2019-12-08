package com.org.simplelab.database.services;

import com.org.simplelab.database.entities.sql.BaseTable;
import com.org.simplelab.database.repositories.sql.BaseRepository;
import org.springframework.stereotype.Component;

@Component
public class HistoryDB extends DBService<BaseTable>{

    @Override
    public BaseRepository<BaseTable> getRepository() {
        return null;
    }

    public boolean addToLabHistory(Object o){
        return true;
    }


}
