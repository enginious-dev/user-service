package com.enginious.userservice.controller;

import com.enginious.userservice.model.Organizzation;
import com.enginious.userservice.repository.OrganizzationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.text.MatchesPattern;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
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

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrganizzationRepository organizzationRepository;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @AfterEach
    public void cleanup() {
        organizzationRepository.deleteAll();
    }

    @Test
    @WithMockUser
    public void get_empty_table_should_return_empty_list() throws Exception {

        mockMvc
                .perform(get("/organizzation"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @WithMockUser
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
    @WithMockUser
    public void get_existing_entity_should_pass() throws Exception {

        Organizzation organizzation = organizzationRepository.save(buildTestEntity());

        mockMvc
                .perform(
                        get("/organizzation/" + organizzation.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", equalTo(name)))
                .andExpect(jsonPath("$.vatNumber", equalTo(vatNumber)))
                .andExpect(jsonPath("$.addedAt", notNullValue()))
                .andExpect(jsonPath("$.applications", hasSize(0)));
    }

    @Test
    @WithMockUser
    public void get_not_existing_entity_should_return_not_found() throws Exception {

        mockMvc
                .perform(
                        get("/organizzation/" + 0))
                .andExpect(status().isNotFound());
    }


    @Test
    @WithMockUser
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
    @WithMockUser
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
    @WithMockUser
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
    @WithMockUser
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
    @WithMockUser
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
    @WithMockUser
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
    @WithMockUser
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

    @Test
    @WithMockUser
    public void delete_existing_entity_should_pass() throws Exception {

        Organizzation organizzation = organizzationRepository.save(buildTestEntity());

        mockMvc
                .perform(
                        delete("/organizzation/" + organizzation.getId()))
                .andExpect(status().isOk());

        assertThat(organizzationRepository.findById(organizzation.getId()).isPresent()).isFalse();
    }

    @Test
    @WithMockUser
    public void delete_not_existing_entity_should_return_not_found() throws Exception {

        mockMvc
                .perform(
                        delete("/organizzation/" + 0))
                .andExpect(status().isNotFound());
    }

    private Organizzation buildTestEntity() {
        return Organizzation
                .builder()
                .name(name)
                .vatNumber(vatNumber)
                .build();
    }
}
