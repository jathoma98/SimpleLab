package com.org.simplelab.database.validators;

import com.org.simplelab.database.DBUtils;
import com.org.simplelab.database.entities.Equipment;
import com.org.simplelab.database.entities.EquipmentProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
public class EquipmentValidator extends Validator<Equipment> {

    private String name;
    private String type;
    private List<EquipmentProperty> properties;

    @Override
    public void validate() throws InvalidFieldException {
        StringBuilder sb = new StringBuilder();

        if (name == null || name.equals(""))
            sb.append(EMPTY_FIELD);

        if (sb.length() > 0)
            throw new InvalidFieldException(sb.toString());
    }

    @Override
    public Equipment build() {
        Equipment e = DBUtils.getMapper().map(this, Equipment.class);
        return e;
    }
}
