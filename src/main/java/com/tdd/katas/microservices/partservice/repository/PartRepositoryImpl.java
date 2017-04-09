package com.tdd.katas.microservices.partservice.repository;

import com.tdd.katas.microservices.partservice.model.PartData;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PartRepositoryImpl implements PartRepository {

    private Map<String,List<PartData>> map = new HashMap<>();

    void store(String vin, List<PartData> partDataList) {
        map.put(vin, partDataList);
    }

    @Override
    public List<PartData> getPartDataList(String vin) {
        return map.get(vin);
    }

}
