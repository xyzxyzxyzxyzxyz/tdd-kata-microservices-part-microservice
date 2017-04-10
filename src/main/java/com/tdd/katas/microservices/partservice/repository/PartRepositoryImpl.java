package com.tdd.katas.microservices.partservice.repository;

import com.tdd.katas.microservices.partservice.model.PartData;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PartRepositoryImpl implements PartRepository {

    private Map<String,List<PartData>> map = new HashMap<>();

    @Override
    public List<PartData> getPartDataList(String vin) {
        return map.get(vin);
    }

    @Override
    public void createPartDataList(String vin, List<PartData> partDataList) throws IllegalStateException {
        if (map.containsKey(vin)) {
            throw new IllegalStateException("Repository already contains a PartData with VIN: ["+vin+"]");
        }
        map.put(vin, partDataList);
    }

    @Override
    public void deleteAllPartDataLists() {
        map.clear();
    }

}
