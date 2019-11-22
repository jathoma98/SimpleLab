package com.org.simplelab.database.validators;

import com.org.simplelab.database.CourseDB;
import com.org.simplelab.database.entities.Course;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Transient;

import java.util.List;

@Getter
@Setter
@ToString
public class CourseValidator extends Validator{

    private String name;
    private String course_id;
    private String description;
    private String _metadata;

    public static final String EMPTY_FIELD = "Fields cannot be empty \n";
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
        //TODO: properly refactor with modelmapper

        Course c = new Course();
        c.setName(name);
        c.setCourse_id(course_id);
        c.setDescription(description);
        c.set_metadata(_metadata);
        return c;
    }
}
