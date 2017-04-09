package com.tdd.katas.microservices.partservice.controller;

import com.tdd.katas.microservices.partservice.model.PartData;
import com.tdd.katas.microservices.partservice.service.PartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/parts")
public class PartController {

    @Autowired
    private PartService partService;

    @GetMapping("/{vinCode}")
    public ResponseEntity<List<PartData>> getPartDataList(@PathVariable String vinCode) {
        List<PartData> partData;

        try {
            partData = partService.getPartDataList(vinCode);
        } catch(Throwable error) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (partData == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(partData, HttpStatus.OK);
    }

}
