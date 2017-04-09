package com.tdd.katas.microservices.partservice.service;

import com.tdd.katas.microservices.partservice.model.PartData;
import com.tdd.katas.microservices.partservice.repository.PartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PartServiceImpl implements PartService {

    @Autowired
    private PartRepository partRepository;

    @Override
    public List<PartData> getPartDataList(String vin) {
        return partRepository.getPartDataList(vin);
    }

}
