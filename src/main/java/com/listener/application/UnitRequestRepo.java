package com.listener.application;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UnitRequestRepo extends JpaRepository<UnitRequest,Long> {




}
