package com.enginious.userservice.controller;

import com.enginious.userservice.model.Organizzation;
import com.enginious.userservice.repository.OrganizzationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.text.MatchesPattern;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.regex.Pattern;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest
public class OrganizzationControllerTest {

    private static final String name = "enginious";
    private static final String vatNumber = "1234567";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrganizzationRepository organizzationRepository;

    @AfterEach
    public void cleanTable() {
        organizzationRepository.deleteAll();
    }

    @Test
    public void get_empty_table_should_return_empty_list() throws Exception {

        mockMvc
                .perform(get("/organizzation"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void get_one_element_table_should_return_one_element_list() throws Exception {

        organizzationRepository.save(buildTestEntity());

        mockMvc
                .perform(get("/organizzation"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", notNullValue()))
                .andExpect(jsonPath("$[0].name", equalTo(name)))
                .andExpect(jsonPath("$[0].vatNumber", equalTo(vatNumber)))
                .andExpect(jsonPath("$[0].addedAt", notNullValue()))
                .andExpect(jsonPath("$[0].applications", hasSize(0)));
    }

    @Test
    public void post_valid_element_should_pass() throws Exception {
        mockMvc
                .perform(
                        post("/organizzation")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(buildTestEntity())))
                .andExpect(status().isCreated())
                .andExpect(header().exists("location"))
                .andExpect(header().string("location", new MatchesPattern(Pattern.compile("^http://.+/organizzation/\\d+$"))));
    }

    @Test
    public void post_null_name_should_return_bad_request() throws Exception {

        Organizzation organizzation = buildTestEntity();
        organizzation.setName(null);

        mockMvc
                .perform(
                        post("/organizzation")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(organizzation)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void post_null_vatNumber_should_return_bad_request() throws Exception {

        Organizzation organizzation = buildTestEntity();
        organizzation.setVatNumber(null);

        mockMvc
                .perform(
                        post("/organizzation")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(organizzation)))
                .andExpect(status().isBadRequest());
    }

    private Organizzation buildTestEntity() {
        return Organizzation
                .builder()
                .name(name)
                .vatNumber(vatNumber)
                .build();
    }
}
