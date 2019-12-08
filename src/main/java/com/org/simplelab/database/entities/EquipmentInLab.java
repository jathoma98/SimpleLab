package com.org.simplelab.database.entities;

/*
* This class use to store same equipment with different property in the lab
* */

//import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.org.simplelab.database.DBUtils;
//import lombok.Data;
//
//import javax.persistence.*;
//import java.util.Set;
//
//@Data
//@Entity(name = DBUtils.EQUIPMENT_IN_LAB_TABLE_NAME)
//@Table(name = DBUtils.EQUIPMENT_IN_LAB_TABLE_NAME)
//public class EquipmentInLab extends BaseTable {
//    @JsonIgnore
//    @ManyToOne(fetch = FetchType.EAGER)
//    protected Lab parentLab;
//
//    @JsonIgnore
//    @ManyToMany(fetch = FetchType.EAGER,
//                mappedBy = "parentLab")
//    protected Equipment parentEquipment;
//
//    @OneToMany(cascade = {CascadeType.ALL},
//            fetch = FetchType.EAGER,
//            mappedBy = "parentEquipment")
//    private Set<EquipmentProperty> properties;
//}
