package com.listener.application;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ParamsMapRepo extends JpaRepository<ParamsMap,Long> {


    @Transactional
    @Query("SELECT r FROM ParamsMap r WHERE r.unitInputId = :unitInputId")
    List<ParamsMap> getByUnitInputId(@Param("unitInputId") Integer unitInputId);


    @Transactional
    @Query("SELECT r FROM ParamsMap r WHERE r.rw = :rw")
    List<ParamsMap> getByExactRW(@Param("rw") String rw);

    @Transactional
    @Query("SELECT r FROM ParamsMap r WHERE r.rw like %:rw%")
    List<ParamsMap> getByRwLike(@Param("rw") String rw);


    @Transactional
    @Query(value = "SELECT * FROM params_map  WHERE rw LIKE '%r%' AND active = 'true'", nativeQuery = true)
    List<ParamsMap> getByActiveAndRead();


}
