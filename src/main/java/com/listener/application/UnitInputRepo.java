package com.listener.application;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UnitInputRepo extends JpaRepository<UnitInput, Long> {

    @Transactional
    @Query("SELECT r FROM UnitInput r WHERE r.inputNumber = :input_number")
    List<UnitInput> getByInput(@Param("input_number") Integer inputNumber);


    @Transactional
    @Query("SELECT r FROM UnitInput r WHERE r.unitNetIdent = :unit_net_ident")
    List<UnitInput> getByUnit(@Param("unit_net_ident") Integer unitNetIdent);

    @Transactional
    @Query("SELECT r FROM UnitInput r WHERE r.unitNetIdent = :unit_net_ident AND r.inputNumber = :input_number")
    List<UnitInput> getByUnitAndInput(@Param("unit_net_ident") Integer unitNetIdent, @Param("input_number") Integer inputNumber);


}
