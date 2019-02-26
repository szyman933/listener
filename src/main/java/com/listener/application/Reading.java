package com.listener.application;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.sql.Timestamp;


@Slf4j
@Entity
@Setter
@Getter
@Table(name = "readings")
@ToString
@NoArgsConstructor
@EqualsAndHashCode
class Reading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
