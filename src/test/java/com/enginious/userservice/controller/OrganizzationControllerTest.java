package com.enginious.userservice.controller;

import com.enginious.userservice.model.Organizzation;
import com.enginious.userservice.repository.OrganizzationRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.text.MatchesPattern;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
public class OrganizzationControllerTest extends ControllerTest {

    @Autowired
    private OrganizzationRepository organizzationRepository;

    protected Organizzation buildTestOrganizzation() {
        return Organizzation
                .builder()
                .name("enginious")
                .vatNumber("123456789")
                .build();
    }

    @Test
    @WithMockUser
    public void post_null_vatNumber_should_return_bad_request() throws Exception {
        Organizzation organizzation = buildTestOrganizzation();
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
    public void post_invalid_size_vatNumber_should_return_bad_request() throws Exception {
        Organizzation organizzation = buildTestOrganizzation();
        organizzation.setVatNumber(StringUtils.EMPTY);

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
        organizzationRepository.save(buildTestOrganizzation());

        mockMvc
                .perform(
                        post("/organizzation")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(buildTestOrganizzation())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", new MatchesPattern(Pattern.compile("(?i)^constraint\\sviolation$"))));
    }

    @Test
    @WithMockUser
    public void put_null_vatNumber_should_return_bad_request() throws Exception {
        Organizzation organizzation = organizzationRepository.save(buildTestOrganizzation());
        organizzation.setVatNumber(null);

        mockMvc
                .perform(
                        put("/organizzation/" + organizzation.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(organizzation)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void put_invalid_size_vatNumber_should_return_bad_request() throws Exception {
        Organizzation organizzation = organizzationRepository.save(buildTestOrganizzation());
        organizzation.setVatNumber(StringUtils.EMPTY);

        mockMvc
                .perform(
                        put("/organizzation/" + organizzation.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(organizzation)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void post_null_name_should_return_bad_request() throws Exception {
        Organizzation organizzation = buildTestOrganizzation();
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
    public void post_invalid_size_name_should_return_bad_request() throws Exception {
        Organizzation organizzation = buildTestOrganizzation();
        organizzation.setName(StringUtils.EMPTY);

        mockMvc
                .perform(
                        post("/organizzation")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(organizzation)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void put_null_name_should_return_bad_request() throws Exception {
        Organizzation organizzation = organizzationRepository.save(buildTestOrganizzation());
        organizzation.setName(null);

        mockMvc
                .perform(
                        put("/organizzation/" + organizzation.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(organizzation)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void put_invalid_size_name_should_return_bad_request() throws Exception {
        Organizzation organizzation = organizzationRepository.save(buildTestOrganizzation());
        organizzation.setName(StringUtils.EMPTY);

        mockMvc
                .perform(
                        put("/organizzation/" + organizzation.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(organizzation)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Override
    @WithMockUser
    public void get_no_entities_in_table_should_return_empty_list() throws Exception {
        mockMvc
                .perform(get("/organizzation"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @Override
    @WithMockUser
    public void get_one_entity_in_table_should_return_one_element_in_list() throws Exception {
        Organizzation organizzation = organizzationRepository.save(buildTestOrganizzation());

        mockMvc
                .perform(get("/organizzation"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", equalTo((int) organizzation.getId())))
                .andExpect(jsonPath("$[0].name", equalTo(organizzation.getName())))
                .andExpect(jsonPath("$[0].vatNumber", equalTo(organizzation.getVatNumber())))
                .andExpect(jsonPath("$[0].addedAt", notNullValue()))
                .andExpect(jsonPath("$[0].applications", hasSize(0)));
    }

    @Test
    @Override
    @WithMockUser
    public void get_existing_entity_should_pass() throws Exception {
        Organizzation organizzation = organizzationRepository.save(buildTestOrganizzation());

        mockMvc
                .perform(
                        get("/organizzation/" + organizzation.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo((int) organizzation.getId())))
                .andExpect(jsonPath("$.name", equalTo(organizzation.getName())))
                .andExpect(jsonPath("$.vatNumber", equalTo(organizzation.getVatNumber())))
                .andExpect(jsonPath("$.addedAt", notNullValue()))
                .andExpect(jsonPath("$.applications", hasSize(0)));
    }

    @Test
    @Override
    @WithMockUser
    public void get_not_existing_entity_should_return_not_found() throws Exception {
        mockMvc
                .perform(
                        get("/organizzation/" + 0))
                .andExpect(status().isNotFound());
    }

    @Test
    @Override
    @WithMockUser
    public void post_valid_entity_should_pass() throws Exception {
        mockMvc
                .perform(
                        post("/organizzation")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(buildTestOrganizzation())))
                .andExpect(status().isCreated())
                .andExpect(header().exists("location"))
                .andExpect(header().string("location", new MatchesPattern(Pattern.compile("^http://.+/organizzation/\\d+$"))));
    }

    @Test
    @Override
    @WithMockUser
    public void put_valid_entity_should_pass() throws Exception {
        Organizzation organizzation = organizzationRepository.save(buildTestOrganizzation());

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
    @Override
    @WithMockUser
    public void put_not_existing_entity_should_return_not_found() throws Exception {

        Organizzation organizzation = organizzationRepository.save(buildTestOrganizzation());

        organizzation.setName("upd1");
        organizzation.setName("upd2");
        organizzation.setAddedAt(LocalDateTime.now());

        mockMvc
                .perform(
                        put("/organizzation/" + 0)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(organizzation)))
                .andExpect(status().isNotFound());
    }

    @Test
    @Override
    @WithMockUser
    public void delete_existing_entity_should_pass() throws Exception {
        Organizzation organizzation = organizzationRepository.save(buildTestOrganizzation());

        mockMvc
                .perform(
                        delete("/organizzation/" + organizzation.getId()))
                .andExpect(status().isOk());

        assertThat(organizzationRepository.findById(organizzation.getId()).isPresent()).isFalse();
    }

    @Test
    @Override
    @WithMockUser
    public void delete_not_existing_entity_should_return_not_found() throws Exception {
        mockMvc
                .perform(
                        delete("/organizzation/" + 0))
                .andExpect(status().isNotFound());
    }

    @Override
    protected void doCleanup() {
        organizzationRepository.deleteAll();
    }
}
