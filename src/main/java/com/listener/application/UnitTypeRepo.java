package com.listener.application;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
interface UnitTypeRepo extends JpaRepository <UnitType,Integer> {

    @Transactional
    @Query("SELECT r FROM UnitType r WHERE r.unitTypeId = :unitType")
    List<UnitType> getByType(@Param("unitType") Integer unitType);

}
