package com.enginious.userservice.controller;

import com.enginious.userservice.model.Application;
import com.enginious.userservice.model.Organizzation;
import com.enginious.userservice.model.enums.Role;
import com.enginious.userservice.repository.ApplicationRepository;
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
import java.util.UUID;
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
public class ApplicationControllerTest {

    private static final String organizzationName = "enginious";
    private static final String organizzationVatNumber = "1234567";
    private static final String applicationName = "UserService";
    private static final String applicationUuid = UUID.randomUUID().toString();
    private static final String applicationSecret = "secret";
    private static final Role applicationRole = Role.ADMIN;

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

    @Autowired
    private ApplicationRepository applicationRepository;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @AfterEach
    public void cleanup() {
        applicationRepository.deleteAll();
        organizzationRepository.deleteAll();
    }

    @Test
    @WithMockUser
    public void get_empty_table_should_return_empty_list() throws Exception {

        mockMvc
                .perform(get("/organizzation/1/application"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @WithMockUser
    public void get_one_element_table_should_return_one_element_list() throws Exception {

        Application application = applicationRepository.save(buildTestApplication(organizzationRepository.save(buildTestOrganizzation())));

        mockMvc
                .perform(get("/organizzation/" + application.getOrganizzation().getId() + "/application"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", equalTo((int) application.getId())))
                .andExpect(jsonPath("$[0].name", equalTo(applicationName)))
                .andExpect(jsonPath("$[0].uuid", equalTo(applicationUuid)))
                .andExpect(jsonPath("$[0].secret", equalTo(applicationSecret)))
                .andExpect(jsonPath("$[0].role", equalTo(applicationRole.name())));
    }

    @Test
    @WithMockUser
    public void get_by_parent_id_and_self_id_should_return_one_element_list() throws Exception {

        Application application = applicationRepository.save(buildTestApplication(organizzationRepository.save(buildTestOrganizzation())));

        mockMvc
                .perform(get("/organizzation/" + application.getOrganizzation().getId() + "/application/" + application.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo((int) application.getId())))
                .andExpect(jsonPath("$.name", equalTo(applicationName)))
                .andExpect(jsonPath("$.uuid", equalTo(applicationUuid)))
                .andExpect(jsonPath("$.secret", equalTo(applicationSecret)))
                .andExpect(jsonPath("$.role", equalTo(applicationRole.name())));
    }

    private Application buildTestApplication(Organizzation organizzation) {
        return Application
                .builder()
                .name(applicationName)
                .uuid(applicationUuid)
                .secret(applicationSecret)
                .role(applicationRole)
                .organizzation(organizzation)
                .build();
    }

    private Organizzation buildTestOrganizzation() {
        return Organizzation
                .builder()
                .name(organizzationName)
                .vatNumber(organizzationVatNumber)
                .build();
    }
}
