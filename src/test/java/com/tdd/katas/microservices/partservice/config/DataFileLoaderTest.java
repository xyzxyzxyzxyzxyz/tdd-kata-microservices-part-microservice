package com.tdd.katas.microservices.partservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tdd.katas.microservices.partservice.model.PartData;
import com.tdd.katas.microservices.partservice.repository.PartRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(SpringRunner.class)
@ActiveProfiles(Profiles.PROFILE_INTEGRATION_TEST)
@ContextConfiguration(classes = { DataFileLoader.class, ObjectMapper.class })
public class DataFileLoaderTest {

    @MockBean
    private Environment environment;
    @MockBean
    private PartRepository partRepository;
    @Autowired
    private DataFileLoader dataFileLoader;

    @Test
    public void No_property_then_no_action() {
        // Property is not present
        given ( environment.getProperty(DataFileLoader.DATA_FILE_PROPERTY) )
                .willReturn(null);

        // Try to load data
        dataFileLoader.loadData();

        // No action has been done
        verifyZeroInteractions(partRepository);
    }

    @Test
    public void With_property_but_no_file_then_no_action() throws IOException {
        // Get a file location for a file that we know it does not exist
        // We create a temporary file and we delete it
        File tempFile = File.createTempFile("With_property_but_no_file_no_action",".json");
        String nonExistingFileLocation = tempFile.getAbsolutePath();
        tempFile.delete();

        // Property is present
        given ( environment.getProperty(DataFileLoader.DATA_FILE_PROPERTY) )
                .willReturn(nonExistingFileLocation);

        // Try to load data
        dataFileLoader.loadData();

        // No action has been done
        verifyZeroInteractions(partRepository);
    }

    @Test
    public void With_property_and_readable_file_but_invalid_data_then_no_action() throws IOException {
        // Get a file location for a file that we know it exists but has invalid data
        // We create a temporary file and fill it with invalid data
        File tempFile = File.createTempFile("With_property_and_readable_file_but_invalid_data_then_no_action",".json");
        String invalidFileLocation = tempFile.getAbsolutePath();
        FileWriter fw = new FileWriter(tempFile);
        fw.write("INVALID_DATA_NO_JSON_FORMAT");
        fw.close();

        // Property is present
        given ( environment.getProperty(DataFileLoader.DATA_FILE_PROPERTY) )
                .willReturn(invalidFileLocation);

        // Try to load data
        dataFileLoader.loadData();

        // No action has been done
        verifyZeroInteractions(partRepository);
    }

    @Test
    public void With_property_and_readable_file_with_valid_data_then_all_data_loaded() throws IOException {
        // Get a file location for a file that we know it exists and has valid data
        // We create a temporary file and fill it with valid data
        File tempFile = File.createTempFile("With_property_and_readable_file_with_valid_data_then_all_data_loaded",".json");
        String validFileLocation = tempFile.getAbsolutePath();
        FileWriter fw = new FileWriter(tempFile);
        fw.write(
                "{														\n" +
                "	\"VINCODE_X\":                                      \n" +
                "		[                                               \n" +
                "			{                                           \n" +
                "				\"partId\": \"PARTID_X0\",              \n" +
                "				\"description\": \"DESCRIPTION_X0\"     \n" +
                "			}                                           \n" +
                "			,                                           \n" +
                "			{                                           \n" +
                "				\"partId\": \"PARTID_X1\",              \n" +
                "				\"description\": \"DESCRIPTION_X1\"     \n" +
                "			}                                           \n" +
                "		]                                               \n" +
                "	,                                                   \n" +
                "	\"VINCODE_Y\":                                      \n" +
                "		[                                               \n" +
                "			{                                           \n" +
                "				\"partId\": \"PARTID_Y0\",              \n" +
                "				\"description\": \"DESCRIPTION_Y0\"     \n" +
                "			}                                           \n" +
                "			,                                           \n" +
                "			{                                           \n" +
                "				\"partId\": \"PARTID_Y1\",              \n" +
                "				\"description\": \"DESCRIPTION_Y1\"     \n" +
                "			}                                           \n" +
                "		]                                               \n" +
                "	,                                                   \n" +
                "	\"VINCODE_Z\":                                      \n" +
                "		[                                               \n" +
                "			{                                           \n" +
                "				\"partId\": \"PARTID_Z0\",              \n" +
                "				\"description\": \"DESCRIPTION_Z0\"     \n" +
                "			}                                           \n" +
                "			,                                           \n" +
                "			{                                           \n" +
                "				\"partId\": \"PARTID_Z1\",              \n" +
                "				\"description\": \"DESCRIPTION_Z1\"     \n" +
                "			}                                           \n" +
                "		]                                               \n" +
                "}                                                      \n"
        );
        fw.close();

        // Property is present
        given ( environment.getProperty(DataFileLoader.DATA_FILE_PROPERTY) )
                .willReturn(validFileLocation);

        // Store the created data somewhere we can control
        Map<String,List<PartData>> createdData = new HashMap<>();
        doAnswer((invocation) -> {
            createdData.put(
                    invocation.getArgumentAt(0, String.class),
                    invocation.getArgumentAt(1, List.class)
            );
            return null;
        }).when(partRepository).createPartDataList(any(), any());

        // Try to load data
        dataFileLoader.loadData();

        // Check that the correct data has been created.
        assertEquals(3, createdData.size());

        assertTrue(createdData.containsKey("VINCODE_X"));
        assertEquals(2, createdData.get("VINCODE_X").size());
        assertEquals(createdData.get("VINCODE_X").get(0).getPartId(), "PARTID_X0");
        assertEquals(createdData.get("VINCODE_X").get(0).getDescription(), "DESCRIPTION_X0");
        assertEquals(createdData.get("VINCODE_X").get(1).getPartId(), "PARTID_X1");
        assertEquals(createdData.get("VINCODE_X").get(1).getDescription(), "DESCRIPTION_X1");

        assertTrue(createdData.containsKey("VINCODE_Y"));
        assertEquals(2, createdData.get("VINCODE_Y").size());
        assertEquals(createdData.get("VINCODE_Y").get(0).getPartId(), "PARTID_Y0");
        assertEquals(createdData.get("VINCODE_Y").get(0).getDescription(), "DESCRIPTION_Y0");
        assertEquals(createdData.get("VINCODE_Y").get(1).getPartId(), "PARTID_Y1");
        assertEquals(createdData.get("VINCODE_Y").get(1).getDescription(), "DESCRIPTION_Y1");

        assertTrue(createdData.containsKey("VINCODE_Z"));
        assertEquals(2, createdData.get("VINCODE_Z").size());
        assertEquals(createdData.get("VINCODE_Z").get(0).getPartId(), "PARTID_Z0");
        assertEquals(createdData.get("VINCODE_Z").get(0).getDescription(), "DESCRIPTION_Z0");
        assertEquals(createdData.get("VINCODE_Z").get(1).getPartId(), "PARTID_Z1");
        assertEquals(createdData.get("VINCODE_Z").get(1).getDescription(), "DESCRIPTION_Z1");
    }
}
