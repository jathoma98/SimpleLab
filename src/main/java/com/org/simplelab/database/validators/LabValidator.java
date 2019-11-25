package com.org.simplelab.database.validators;

import com.org.simplelab.database.DBUtils;
import com.org.simplelab.database.entities.Lab;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.ModelMapper;

@Getter
@Setter
@ToString
public class LabValidator extends Validator<Lab> {

    private String name;
    private String _metadata;

    public static final String NO_NAME_ERROR = "You must provide a name for this lab. \n";

    @Override
    public void validate() throws InvalidFieldException {
        StringBuilder sb = new StringBuilder();
        System.out.println(name);

        if (name == null || name.equals(""))
            sb.append(NO_NAME_ERROR);

        if (sb.length() > 0)
            throw new InvalidFieldException(sb.toString());
    }

    @Override
    public Lab build() {
        ModelMapper mm = DBUtils.getMapper();
        Lab lab = mm.map(this, Lab.class);
        return lab;
    }
}
