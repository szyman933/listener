package com.listener.application;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "params_map")
@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
class ParamsMap {


    @Id
    @Column(name = "id")
    private Integer id;


    @Column(name = "input_device_id")
    private Integer inputDeviceId;

    @Column(name = "unit_input_id")
    private  Integer unitInputId;

    @Column(name = "rw")
    private String rw;

    @Column(name = "index")
    private  Integer index;

    @Column(name = "active")
    private String active;

    @Column(name = "description")
    private String description;

}
