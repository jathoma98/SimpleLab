package com.org.simplelab.database.validators;

import com.org.simplelab.database.DBUtils;
import com.org.simplelab.database.entities.sql.Equipment;
import com.org.simplelab.database.entities.sql.EquipmentProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Collection;

@Getter
@Setter
@ToString
public class EquipmentValidator extends Validator<Equipment> {

    private String name;
    private String type;
    private Collection<EquipmentProperty> properties;
    private ImageFileValidator img;
    private String _metadata;

    @Override
    public void validate() throws InvalidFieldException {
        StringBuilder sb = new StringBuilder();

        if (name == null || name.equals(""))
            sb.append(EMPTY_FIELD);

//        validate image if it exists
        if (img != null) {
            try {
                img.validate();
            } catch (InvalidFieldException e) {
                sb.append(e.getMessage());
            }
        }

        if (sb.length() > 0)
            throw new InvalidFieldException(sb.toString());
    }

    @Override
    public Equipment build() {
        Equipment e = DBUtils.getMapper().map(this, Equipment.class);
        if (img != null) {
            e.setImg(img.build());
        }
        e.getProperties().forEach((p)->p.setParentEquipment(e));
        return e;
    }
}
