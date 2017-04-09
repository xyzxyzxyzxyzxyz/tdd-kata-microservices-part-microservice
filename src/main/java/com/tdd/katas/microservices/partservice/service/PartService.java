package com.tdd.katas.microservices.partservice.service;

import com.tdd.katas.microservices.partservice.model.PartData;

import java.util.List;

public interface PartService {
    List<PartData> getPartDataList(String vin);
}
