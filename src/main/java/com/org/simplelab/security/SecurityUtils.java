package com.org.simplelab.security;

/**
 * Utility methods and static fields for Spring Security.
 */
public class SecurityUtils {


    public static final String AUTH_STUDENT = "student";
    public static final String AUTH_TEACHER = "teacher";
    public static final String AUTH_ERROR = "invalid_auth";


    public static final String HAS_STUDENT_AUTHORITY = "hasAuthority('" + AUTH_STUDENT + "')";
    public static final String HAS_TEACHER_AUTHORITY = "hasAuthority('" + AUTH_TEACHER + "')";

    public static String getRoleFromDB(String role){
        if (role.equals("student")) {
            return AUTH_STUDENT;
        } else if (role.equals("teacher")){
            return AUTH_TEACHER;
        }
        return AUTH_ERROR;
    }

}
