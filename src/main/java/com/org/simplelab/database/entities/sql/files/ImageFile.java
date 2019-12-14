package com.org.simplelab.database.entities.sql.files;

import com.org.simplelab.database.DBUtils;
import com.org.simplelab.database.entities.sql.Equipment;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity(name = DBUtils.IMAGE_FILE_TABLE_NAME)
@Table(name = DBUtils.IMAGE_FILE_TABLE_NAME)
@Data
public class ImageFile extends File {

    @OneToOne(mappedBy = "img")
    private Equipment source;


}
