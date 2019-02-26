package com.listener.application;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
interface UnitRequestRepo extends JpaRepository<UnitRequest,Integer> {

    @Transactional
    List<UnitRequest> findBySendDateIsNull();


}
