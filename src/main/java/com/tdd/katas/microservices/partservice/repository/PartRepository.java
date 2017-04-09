package com.tdd.katas.microservices.partservice.repository;


import com.tdd.katas.microservices.partservice.model.PartData;

import java.util.List;

public interface PartRepository {
    List<PartData> getPartDataList(String vin);
}
