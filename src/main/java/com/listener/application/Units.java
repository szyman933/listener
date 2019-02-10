package com.listener.application;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "unit")
@Setter
@Getter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
class Units implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "unit_type")
    private Integer unitType;

    @Column(name = "net_ident")
    public Integer netIdent;

    @Column(name = "reg_date")
    private Timestamp regDate;

}
