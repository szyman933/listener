package com.listener.application;

import lombok.*;
import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "unit")
@Setter
@Getter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
class Unit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "unit_type")
    private Integer unitType;

    @Column(name = "net_ident")
    public int netIdent;

    @Column(name = "reg_date")
    private Timestamp regDate;


}
