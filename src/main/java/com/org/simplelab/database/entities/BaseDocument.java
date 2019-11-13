package com.org.simplelab.database.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
@ToString
public class BaseDocument {

    @Id
    private String _id;
    private String createdDate;
    public String _metadata;

    public BaseDocument(){
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy HH:mm");
        this.createdDate = df.format(date);
    }

}
