package com.listener.application;


import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table( name = "unit_input")
@Setter
@Getter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public class UnitInput implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "unit_net_ident")
    private Integer unitNetIdent;

    @Column(name = "input_type_id")
    private Integer inputTypeId;

    @Column(name = "input_number")
    private Integer inputNumber;


}
