package com.listener.application;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Setter
@Getter
@ToString
@Table(name = "unit_type")
@Entity
class UnitType {


    @Id
    @Column(name = "unit_type")
    private Integer unitTypeId;

    @Column(name = "unit_name")
    private String unitName;

}
