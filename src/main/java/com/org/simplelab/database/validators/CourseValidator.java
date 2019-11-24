package com.org.simplelab.database.validators;

import com.org.simplelab.database.DBUtils;
import com.org.simplelab.database.entities.Course;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CourseValidator extends Validator<Course>{

    private String name;
    private String course_id;
    private String description;
    private String _metadata;

    public static final String DUPLICATE_ID = "That course ID has been taken. \n";

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
        Course c = DBUtils.MAPPER.map(this, Course.class);
        return c;
    }
}
