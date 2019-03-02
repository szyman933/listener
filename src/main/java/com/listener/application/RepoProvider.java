package com.listener.application;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Getter
@Component
public class RepoProvider {

    @Autowired
    UnitInputRepo unitInputRepo;

    @Autowired
    UnitRepo unitRepo;

    @Autowired
    UnitTypeRepo unitTypeRepo;

    @Autowired
    ReadingsRepo readingsRepo;

    @Autowired
    UnitRequestRepo unitRequestRepo;

    @Autowired
    ParamsMapRepo paramsMapRepo;
    
}
