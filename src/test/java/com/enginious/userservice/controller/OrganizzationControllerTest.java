package com.enginious.userservice.controller;

import com.enginious.userservice.model.Organizzation;
import com.enginious.userservice.repository.OrganizzationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.text.MatchesPattern;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
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

    @TestConfiguration
    static class OrganizzationControllerTestConfig {

        @Bean
        public ObjectMapper objectMapper() {
            return (new ObjectMapper()).registerModule(new JavaTimeModule());
        }
    }

    @Autowired
    private ObjectMapper objectMapper;

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
    public void post_valid_entity_should_pass() throws Exception {
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
    public void post_invalid_name_should_return_bad_request() throws Exception {

        Organizzation organizzation = buildTestEntity();
        organizzation.setName("a");

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

    @Test
    public void post_invalid_vatNumber_should_return_bad_request() throws Exception {

        Organizzation organizzation = buildTestEntity();
        organizzation.setVatNumber("a");

        mockMvc
                .perform(
                        post("/organizzation")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(organizzation)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void post_duplicate_vatNumber_should_return_bad_request() throws Exception {

        Organizzation organizzation = buildTestEntity();

        mockMvc
                .perform(
                        post("/organizzation")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(organizzation)))
                .andExpect(status().isCreated());

        mockMvc
                .perform(
                        post("/organizzation")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(organizzation)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void put_valid_entity_should_pass() throws Exception {

        Organizzation organizzation = organizzationRepository.save(buildTestEntity());

        LocalDateTime addedAt = organizzation.getAddedAt();

        organizzation.setName("upd1");
        organizzation.setName("upd2");
        organizzation.setAddedAt(LocalDateTime.now());

        mockMvc
                .perform(
                        put("/organizzation/" + organizzation.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(organizzation)))
                .andExpect(status().isOk());

        Organizzation updated = organizzationRepository.findById(organizzation.getId()).orElseThrow(EntityNotFoundException::new);

        assertThat(updated.getId()).isEqualTo(organizzation.getId());
        assertThat(updated.getName()).isEqualTo(organizzation.getName());
        assertThat(updated.getAddedAt()).isEqualTo(addedAt);
        assertThat(updated.getAddedAt()).isNotEqualTo(organizzation.getAddedAt());
    }

    private Organizzation buildTestEntity() {
        return Organizzation
                .builder()
                .name(name)
                .vatNumber(vatNumber)
                .build();
    }
}
