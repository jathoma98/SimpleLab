package com.org.simplelab.utils;

import com.org.simplelab.database.entities.sql.BaseTable;
import com.org.simplelab.database.services.SQLService;

import java.util.List;
import java.util.Random;

import static com.org.simplelab.SpringTestConfig.metadata;

public class DBTestUtils extends TestUtils {

    public static <T extends BaseTable> long insertAndGetId(T toInsert, SQLService<T> db) throws Exception{
        Random rand = new Random();
        String save_data = metadata + Double.toString(rand.nextDouble());
        toInsert.set_metadata(save_data);
        System.out.println(toInsert.get_metadata());
        db.insert(toInsert);
        List<T> found = db.getRepository().findBy_metadata(save_data);
        return found.get(0).getId();
    }

}
