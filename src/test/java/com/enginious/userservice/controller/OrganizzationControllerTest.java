package com.enginious.userservice.controller;

import com.enginious.userservice.model.Organizzation;
import com.enginious.userservice.repository.OrganizzationRepository;
import lombok.extern.slf4j.Slf4j;
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
public class OrganizzationControllerTest extends ControllerTest<Organizzation> {

    @Autowired
    private OrganizzationRepository organizzationRepository;

    @Override
    protected Organizzation buildTestEntity() {
        return Organizzation
                .builder()
                .name("enginious")
                .vatNumber("123456789")
                .build();
    }

    @Override
    protected void doCleanup() {
        organizzationRepository.deleteAll();
    }

    @Override
    @Test
    @WithMockUser
    public void get_no_entities_in_table_should_return_empty_list() throws Exception {
        mockMvc
                .perform(get("/organizzation"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Override
    @Test
    @WithMockUser
    public void get_one_entity_in_table_should_return_one_element_in_list() throws Exception {
        Organizzation organizzation = organizzationRepository.save(buildTestEntity());

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

    @Override
    @Test
    @WithMockUser
    public void get_existing_entity_should_pass() throws Exception {
        Organizzation organizzation = organizzationRepository.save(buildTestEntity());

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

    @Override
    @Test
    @WithMockUser
    public void get_not_existing_entity_should_return_not_found() throws Exception {
        mockMvc
                .perform(
                        get("/organizzation/" + 0))
                .andExpect(status().isNotFound());
    }

    @Override
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

    @Override
    @Test
    @WithMockUser
    public void post_invalid_entity_should_return_bad_request() throws Exception {
        Organizzation organizzation = buildTestEntity();
        organizzation.setName(null);

        mockMvc
                .perform(
                        post("/organizzation")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(organizzation)))
                .andExpect(status().isBadRequest());
    }

    @Override
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

    @Override
    @Test
    @WithMockUser
    public void put_not_existing_entity_should_return_not_found() throws Exception {

        Organizzation organizzation = organizzationRepository.save(buildTestEntity());

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

    @Override
    @Test
    @WithMockUser
    public void put_invalid_entity_should_return_bad_request() throws Exception {
        Organizzation organizzation = organizzationRepository.save(buildTestEntity());

        organizzation.setName("u");
        organizzation.setVatNumber("upd2");
        organizzation.setAddedAt(LocalDateTime.now());

        mockMvc
                .perform(
                        put("/organizzation/" + organizzation.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(organizzation)))
                .andExpect(status().isBadRequest());
    }

    @Override
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

    @Override
    @Test
    @WithMockUser
    public void delete_not_existing_entity_should_return_not_found() throws Exception {
        mockMvc
                .perform(
                        delete("/organizzation/" + 0))
                .andExpect(status().isNotFound());
    }
}
