package com.listener.application;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
interface ReadingsRepo extends JpaRepository <Reading, Long> {

    @Transactional
    @Query("SELECT r FROM Reading r WHERE r.unitId = :unitId")
    List<Reading> getByUnit(@Param("unitId") Integer unitId);


}
