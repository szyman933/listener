package com.listener.application;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
interface UnitRepo extends JpaRepository<Unit, Long> {


    @Transactional
    @Query("SELECT r FROM Unit r WHERE r.netIdent = :netIdent")
    List<Unit> getByUnit(@Param("netIdent") Integer netIdent);

}
