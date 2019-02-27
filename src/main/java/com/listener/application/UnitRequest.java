package com.listener.application;

import lombok.*;
import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table( name = "unit_request")
@Setter
@Getter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
 class UnitRequest implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "unit_net_ident")
    private Integer unitNetIdent;

    @Column(name = "request_type")
    private Integer requestType;

    @Column(name = "reg_date")
    private Timestamp regDate;

    @Column(name = "send_date")
    private Timestamp sendDate;

    @Column(name = "unit_input_number")
    private Integer unitInputNumber;

    @Column(name = "value")
    private Integer value;

   @Column(name = "register")
   private Integer register;


}
