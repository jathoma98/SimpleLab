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
        db.insert(toInsert);
        List<T> found = db.getRepository().findBy_metadata(save_data);
        return found.get(0).getId();
    }

    public static <T extends BaseTable> T find(T toFind, SQLService<T> db) throws Exception{
        if (toFind.get_metadata() == null){
            throw new Exception("***TEST***: Can only call find() with defined metadata");
        }
        List<T> found = db.getRepository().findBy_metadata(toFind.get_metadata());
        if (found.size() == 0){
            throw new Exception("***TEST*** Entity could not be found in DB.");
        }
        if (found.size() > 1){
            throw new Exception("***TEST*** Multiple copies found in DB.");
        }
        return found.get(0);
    }

    public static <T extends BaseTable> boolean testEquals(T a, T b){
        a.setNew(false);
        a.set_metadata("");
        b.setNew(false);
        b.set_metadata("");
        return a.equals(b);
    }

}
