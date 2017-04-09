package com.tdd.katas.microservices.partservice.service;

import com.tdd.katas.microservices.partservice.model.PartData;
import com.tdd.katas.microservices.partservice.repository.PartRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = PartServiceImpl.class)
public class PartServiceImplTest {
    @Autowired
    private PartService partService;

    @MockBean
    private PartRepository partRepository;

    @Test
    public void The_service_delegates_the_calls_to_the_repository() {
        String VIN_CODE = "XXX";

        List<PartData> expectedPartDataList = Arrays.asList(
                new PartData("1","Wheels"),
                new PartData("2","Doors")
        );

        given(partRepository.getPartDataList(VIN_CODE)).willReturn(expectedPartDataList);

        List<PartData> actualPartDataList = partService.getPartDataList(VIN_CODE);

        // The service must delegate the call to the repository with the same input
        verify(partRepository).getPartDataList(VIN_CODE);

        assertListEquals("The service should return the PartData list as provided by the repository", expectedPartDataList, actualPartDataList);
    }

    private void assertListEquals(String message, List<?> list1, List<?> list2) {
        assertEquals(message, list1.size(), list2.size());
        assertTrue(message, list1.containsAll(list2));
        assertTrue(message, list2.containsAll(list1));
    }

    @Test
    public void The_service_propagates_the_errors_from_the_repository() {

        given(partRepository.getPartDataList(any())).willThrow(new IllegalStateException("database not ready"));

        try {
            partRepository.getPartDataList(any());
            fail("Should have thrown an exception");
        } catch (IllegalStateException e) {
            // The error has been propagated by the service
        }

        // The service must delegate the call to the repository with the same input
        verify(partRepository).getPartDataList(any());

    }

}
