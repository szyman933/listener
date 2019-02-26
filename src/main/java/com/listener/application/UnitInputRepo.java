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
    @Query("SELECT r FROM UnitInput r WHERE r.inputNumber = :inputNumber")
    List<UnitInput> getByInput(@Param("inputNumber") Integer inputNumber);


    @Transactional
    @Query("SELECT r FROM UnitInput r WHERE r.unitNetIdent = :unitNetIdent")
    List<UnitInput> getByUnit(@Param("unitNetIdent") Integer unitNetIdent);

    @Transactional
    @Query("SELECT r FROM UnitInput r WHERE r.unitNetIdent = :unitNetIdent AND r.inputNumber = :inputNumber")
    List<UnitInput> getByUnitAndInput(@Param("unitNetIdent") Integer unitNetIdent, @Param("inputNumber") Integer inputNumber);


}
