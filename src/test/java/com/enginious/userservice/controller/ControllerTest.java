package com.enginious.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public abstract class ControllerTest {

    @TestConfiguration
    static class ControllerTestConfig {
        @Bean
        public ObjectMapper objectMapper() {
            return (new ObjectMapper()).registerModule(new JavaTimeModule());
        }
    }

    protected MockMvc mockMvc;

    @Autowired
    protected WebApplicationContext context;

    @Autowired
    protected ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @AfterEach
    public void cleanup() {
        doCleanup();
    }

    public abstract void get_no_entities_in_table_should_return_empty_list() throws Exception;

    public abstract void get_one_entity_in_table_should_return_one_element_in_list() throws Exception;

    public abstract void get_existing_entity_should_pass() throws Exception;

    public abstract void get_not_existing_entity_should_return_not_found() throws Exception;

    public abstract void post_valid_entity_should_pass() throws Exception;

    public abstract void put_valid_entity_should_pass() throws Exception;

    public abstract void put_not_existing_entity_should_return_not_found() throws Exception;

    public abstract void delete_existing_entity_should_pass() throws Exception;

    public abstract void delete_not_existing_entity_should_return_not_found() throws Exception;

    protected abstract void doCleanup();
}
