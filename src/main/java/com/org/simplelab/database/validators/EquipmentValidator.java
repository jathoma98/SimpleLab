package com.org.simplelab.database.validators;

import com.org.simplelab.database.DBUtils;
import com.org.simplelab.database.entities.Equipment;

public class EquipmentValidator extends Validator {

    private String name;

    @Override
    public void validate() throws InvalidFieldException {
        StringBuilder sb = new StringBuilder();

        if (name == null || name.equals(""))
            sb.append(EMPTY_FIELD);

        if (sb.length() > 0)
            throw new InvalidFieldException(sb.toString());
    }

    @Override
    public Object build() {
        Equipment e = DBUtils.MAPPER.map(this, Equipment.class);
        return e;
    }
}
