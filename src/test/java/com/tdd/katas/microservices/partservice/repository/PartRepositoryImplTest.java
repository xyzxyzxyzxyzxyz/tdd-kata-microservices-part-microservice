package com.tdd.katas.microservices.partservice.repository;

import com.tdd.katas.microservices.partservice.model.PartData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = PartRepositoryImpl.class)
public class PartRepositoryImplTest
{

    @Autowired
    private PartRepositoryImpl partRepository;

    @Test
    public void The_repository_returns_valid_output_for_valid_input() throws Exception {
        String VIN = "XXX";

        List<PartData> expectedPartDataList = Arrays.asList(
                new PartData("1","Wheels"),
                new PartData("2","Doors")
        );

        partRepository.store(VIN, expectedPartDataList);

        List<PartData> actualPartDataList =  partRepository.getPartDataList(VIN);

        assertListEquals("Should return the stored part data list", expectedPartDataList, actualPartDataList);
    }

    private void assertListEquals(String message, List<?> list1, List<?> list2) {
        assertEquals(message, list1.size(), list2.size());
        assertTrue(message, list1.containsAll(list2));
        assertTrue(message, list2.containsAll(list1));
    }

    @Test
    public void The_repository_returns_null_for_invalid_input() throws Exception {
        String EXISTING_VIN = "XXX";
        String NON_EXISTING_VIN = "YYY";

        List<PartData> existingPartDataList = Arrays.asList(
                new PartData("1","Wheels"),
                new PartData("2","Doors")
        );
        partRepository.store(EXISTING_VIN, existingPartDataList);

        List<PartData> nonExistingPartDataList =  partRepository.getPartDataList(NON_EXISTING_VIN);

        assertNull("The part data list should not exist in the database", nonExistingPartDataList);
    }

}
