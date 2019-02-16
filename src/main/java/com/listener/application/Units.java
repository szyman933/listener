package com.listener.application;

import lombok.*;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "unit")
@Setter
@Getter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
class Units implements Frame {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "unit_type")
    private Integer unitType;

    @Column(name = "net_ident")
    public int netIdent;

    @Column(name = "reg_date")
    private Timestamp regDate;

    @Override
    public void parse(String s, int unit) {
        Date date = new Date();
        netIdent = unit;
        unitType = Integer.getInteger(s.substring(Protocol.getHeader(),Protocol.getHeader()+Protocol.getInstallUnitType()));
        regDate = new Timestamp(date.getTime());
    }


}
