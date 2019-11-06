package com.org.simplelab.database.validators;

import com.org.simplelab.database.entities.Course;

public class CourseValidator extends Validator{

    private String name;
    private String course_id;
    private String description;

    public static final String EMPTY_FIELD = "Fields cannot be empty \n";

    @Override
    public void validate() throws InvalidFieldException {
        StringBuilder sb = new StringBuilder();
        int[] lengths = {name.length(), course_id.length(), description.length()};
        for (int length: lengths){
            if (length == 0){
                sb.append(EMPTY_FIELD);
                break;
            }
        }

        if (sb.length() > 0)
            throw new InvalidFieldException(sb.toString());
    }

    @Override
    public Course build() {
        Course c = new Course();
        c.setName(name);
        c.setDescription(description);
        c.setCourse_id(course_id);
        return c;
    }
}
