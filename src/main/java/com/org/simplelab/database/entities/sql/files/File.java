package com.org.simplelab.database.entities.sql.files;

import com.org.simplelab.database.entities.sql.BaseTable;
import lombok.Data;

import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Data
public abstract class File extends BaseTable {

    private String fileName;
    private String fileType;

    //TODO: add uploader field

    @Lob
    private byte[] data;

}
