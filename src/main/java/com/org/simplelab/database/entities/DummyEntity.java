package com.org.simplelab.database.entities;

import java.util.LinkedList;
import java.util.List;

public class DummyEntity {
    static private DummyEntity obj = null;
    static public List<Course> list_course;
    static public List<String> list_ulid;
    static public List<String> list_uuid;

    private DummyEntity(){
        //list of list_ulid
        list_ulid = new LinkedList<>();
        list_uuid = new LinkedList<>();
        for(int i = 0; i < 10; i++){
            list_ulid.add(Integer.toString(i*i));
            list_uuid.add(Integer.toString(i*i/2));
        }

        //list of course
        list_course = new LinkedList<>();
        for(int i = 0; i< 10; i++){
            Course c = new Course(Integer.toString(i), "course_"+ i, i + "-" + i + "-" + "2019", Integer.toString(i*2), list_uuid, list_ulid);
            System.out.println(c);
            list_course.add(c);
        }

        //list of 
    }

    static public DummyEntity getObj(){
        if (obj != null) return obj;
        obj = new DummyEntity();
        return obj;
    }

}
