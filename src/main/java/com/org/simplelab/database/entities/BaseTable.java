package com.org.simplelab.database.entities;

import lombok.Data;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@MappedSuperclass
public class BaseTable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private long id;

    @Column(name = "created_date")
    private String createdDate;

    public String _metadata;

    public BaseTable(){
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy HH:mm");
        this.createdDate = df.format(date);
    }


}
