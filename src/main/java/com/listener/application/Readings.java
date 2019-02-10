package com.listener.application;

import lombok.*;
import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Setter
@Getter
@Table(name = "readings")
@ToString
@NoArgsConstructor
@EqualsAndHashCode
class Readings implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "unit_id")
    private Integer unitId;

    @Column(name = "unitInputId")
    private Integer unitInputId;

    @Column(name = "value")
    private Integer value;

    @Column(name = "read_date")
    private Timestamp readDate;

    @Column(name = "param_id")
    private Integer paramId;

}
