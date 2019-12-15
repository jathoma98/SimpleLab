package com.org.simplelab.database.validators;

import com.org.simplelab.database.DBUtils;
import com.org.simplelab.database.entities.sql.files.ImageFile;
import lombok.Data;

@Data
public class ImageFileValidator extends Validator<ImageFile> {

    private String fileName;
    private String fileType;
    private byte[] data;
    private String _metadata;

    @Override
    public void validate() throws InvalidFieldException {
        //TODO: implement this
    }

    @Override
    public ImageFile build() {
        return DBUtils.getMapper().map(this, ImageFile.class);
    }
}