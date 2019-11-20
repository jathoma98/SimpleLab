package com.org.simplelab.security;

public class SecurityUtils {

    public static final String ROLE_STUDENT = "ROLE_STUDENT";
    public static final String ROLE_TEACHER = "ROLE_TEACHER";

    public static String toRoleFromDB(String db_role){
        if (db_role.equals("teacher"))
            return ROLE_TEACHER;
        return ROLE_STUDENT;
    }

}
