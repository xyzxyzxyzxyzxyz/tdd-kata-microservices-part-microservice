package com.tdd.katas.microservices.partservice.controller;

import com.tdd.katas.microservices.partservice.model.PartData;
import com.tdd.katas.microservices.partservice.service.PartService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = PartController.class)
@WithMockUser
public class PartControllerTest {
    @Autowired
    private PartController partController;

    @MockBean
    private PartService partService;

    @Autowired
    private MockMvc mvc;

    @Test
    public void Can_create_instance() {
        assertNotNull("Should be able to create instance", partController);
    }

    @Test
    public void It_returns_valid_data_for_a_valid_input() throws Exception {

        final String VIN_CODE = "XXX";

        List<PartData> expectedPartDataList = Arrays.asList(
                new PartData("1","Wheels"),
                new PartData("2","Doors")
        );

        given(partService.getPartDataList(VIN_CODE)).willReturn(expectedPartDataList);

        this.mvc.perform(
                    get("/parts/" + VIN_CODE)
                    .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[0].partId", is(expectedPartDataList.get(0).getPartId())))
                .andExpect(jsonPath("$[0].description", is(expectedPartDataList.get(0).getDescription())))
                .andExpect(jsonPath("$[1].partId", is(expectedPartDataList.get(1).getPartId())))
                .andExpect(jsonPath("$[1].description", is(expectedPartDataList.get(1).getDescription())));

        verify(partService).getPartDataList(VIN_CODE);

    }

    @Test
    public void It_returns_404_if_the_customerId_does_not_exist() throws Exception {

        given(partService.getPartDataList(any())).willReturn(null);

        this.mvc
                .perform(
                        get("/parts/" + "PEPITO")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());

        verify(partService).getPartDataList(any());

    }

    @Test
    public void It_returns_500_if_the_service_throws_an_error() throws Exception {

        final String VIN_CODE = "XXX";

        given(partService.getPartDataList(any())).willThrow(
                new IllegalStateException("service is not ready"));

        this.mvc
                .perform(
                        get("/parts/" + VIN_CODE)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isInternalServerError());

        verify(partService).getPartDataList(any());

    }

}
