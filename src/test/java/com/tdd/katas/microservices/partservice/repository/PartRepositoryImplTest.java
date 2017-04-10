package com.tdd.katas.microservices.partservice.repository;

import com.tdd.katas.microservices.partservice.model.PartData;
import org.junit.Before;
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

    @Before
    public void setUp() {
        // Clear repository between tests
        partRepository.deleteAllPartDataLists();
    }

    @Test
    public void The_repository_returns_valid_output_for_valid_input() throws Exception {
        String VIN = "XXX";

        List<PartData> expectedPartDataList = Arrays.asList(
                new PartData("1","Wheels"),
                new PartData("2","Doors")
        );

        partRepository.createPartDataList(VIN, expectedPartDataList);

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
        partRepository.createPartDataList(EXISTING_VIN, existingPartDataList);

        List<PartData> nonExistingPartDataList =  partRepository.getPartDataList(NON_EXISTING_VIN);

        assertNull("The part data list should not exist in the database", nonExistingPartDataList);
    }

    @Test
    public void The_repository_accepts_creating_a_non_existing_vin() throws Exception {
        final String VIN = "X";

        List<PartData> expectedPartDataList = Arrays.asList(new PartData("PARTID_X", "DESCRIPTION_X"));

        partRepository.createPartDataList(VIN, expectedPartDataList);

        List<PartData> actualPartDataList =  partRepository.getPartDataList(VIN);

        assertEquals("The part data list should exist in the repository after creation", expectedPartDataList, actualPartDataList);
    }

    @Test
    public void The_repository_does_not_accept_creating_an_already_existing_vin() throws Exception {
        final String VIN = "X";

        List<PartData> expectedPartDataList = Arrays.asList(new PartData("PARTID_X", "DESCRIPTION_X"));

        partRepository.createPartDataList(VIN, expectedPartDataList);

        List<PartData> actualPartDataList =  partRepository.getPartDataList(VIN);

        assertEquals("The part data list should exist in the repository after creation", expectedPartDataList, actualPartDataList);

        try {
            partRepository.createPartDataList(VIN, expectedPartDataList);
            fail("Should not have accepted the creation of an already existing VIN");
        }
        catch (IllegalStateException e) {
            // Ok
        }

    }

    @Test
    public void The_repository_allows_deleting_all_the_data() throws Exception {
        final String VIN_X = "VIN_X";
        final String VIN_Y = "VIN_Y";

        partRepository.createPartDataList(VIN_X, Arrays.asList(new PartData("PARTID_X", "DESCRIPTION_X")));
        partRepository.createPartDataList(VIN_Y, Arrays.asList(new PartData("PARTID_Y", "DESCRIPTION_Y")));

        assertNotNull("The part data list should exist after creation", partRepository.getPartDataList(VIN_X));
        assertNotNull("The part data list should exist after creation", partRepository.getPartDataList(VIN_Y));

        partRepository.deleteAllPartDataLists();

        assertNull("The part data list should not exist after clearing the repository", partRepository.getPartDataList(VIN_X));
        assertNull("The part data list should not exist after clearing the repository", partRepository.getPartDataList(VIN_Y));
    }

}
