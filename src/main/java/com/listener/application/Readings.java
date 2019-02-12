package com.listener.application;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Slf4j
@Entity
@Setter
@Getter
@Table(name = "readings")
@ToString
@NoArgsConstructor
@EqualsAndHashCode
class Readings implements Frame {

    private int readCount;

    private List<Integer> reads;

    private int input;

    private List<Integer> registers;

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


    @Override
    public void parse(String s, int unit) {
        readCount(s);
        parseReads(s);
        unitId = unit;
    }


    void readCount(String msg) {

        if (msg != null && !msg.isEmpty()) {
            int readLoadLength = msg.length() - Protocol.getHeader();
            readCount = readLoadLength / (Protocol.getReadInput() + Protocol.getReadRegister() + Protocol.getReadData());
        } else {
            throw new IllegalArgumentException("Message can't be NULL or Empty !!!");
        }

    }


    void parseReads(String msg) {
        int cursor = Protocol.getHeader();
        input = Integer.getInteger(msg.substring(cursor, cursor + Protocol.getReadInput()));
        cursor = cursor + Protocol.getReadInput();
        if (readCount > 0) {


            for (int i = 0; i < readCount; i++) {
                registers.add(Integer.getInteger(msg.substring(cursor, cursor + Protocol.getReadRegister())));
                cursor += Protocol.getReadRegister();
                reads.add(Integer.getInteger(msg.substring(cursor, cursor + Protocol.getReadData())));
                cursor = Protocol.getReadData();

            }


        } else {
            log.info("Readings frame has no reads !!");
        }


    }


}
