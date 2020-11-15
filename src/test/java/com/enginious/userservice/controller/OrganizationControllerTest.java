package com.enginious.userservice.controller;

import com.enginious.userservice.model.Organization;
import com.enginious.userservice.repository.OrganizationRepository;
import com.enginious.userservice.service.organization.dto.CreateOrganizationRequest;
import com.enginious.userservice.service.organization.dto.UpdateOrganizationRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.text.MatchesPattern;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import javax.persistence.EntityNotFoundException;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
public class OrganizationControllerTest extends ControllerTest {

    @Autowired
    private OrganizationRepository organizationRepository;

    private Organization buildTestOrganization() {
        return Organization
                .builder()
                .name("enginious")
                .vatNumber("123456789")
                .build();
    }

    @Test
    @WithMockUser
    public void post_null_vatNumber_should_return_bad_request() throws Exception {
        Organization organization = buildTestOrganization();
        CreateOrganizationRequest request = CreateOrganizationRequest
                .builder()
                .name(organization.getName())
                .build();
        mockMvc
                .perform(
                        post("/organization")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void post_invalid_size_vatNumber_should_return_bad_request() throws Exception {
        Organization organization = buildTestOrganization();
        CreateOrganizationRequest request = CreateOrganizationRequest
                .builder()
                .name(organization.getName())
                .vatNumber(StringUtils.EMPTY)
                .build();
        mockMvc
                .perform(
                        post("/organization")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void post_duplicate_vatNumber_should_return_bad_request() throws Exception {
        Organization organization = organizationRepository.save(buildTestOrganization());
        CreateOrganizationRequest request = CreateOrganizationRequest
                .builder()
                .name(organization.getName())
                .vatNumber(organization.getVatNumber())
                .build();
        mockMvc
                .perform(
                        post("/organization")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", new MatchesPattern(Pattern.compile("(?i)^constraint\\sviolation$"))));
    }

    @Test
    @WithMockUser
    public void put_null_vatNumber_should_return_bad_request() throws Exception {
        Organization organization = organizationRepository.save(buildTestOrganization());
        UpdateOrganizationRequest request = UpdateOrganizationRequest
                .builder()
                .name(organization.getName())
                .build();
        mockMvc
                .perform(
                        put("/organization/" + organization.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void put_invalid_size_vatNumber_should_return_bad_request() throws Exception {
        Organization organization = organizationRepository.save(buildTestOrganization());
        UpdateOrganizationRequest request = UpdateOrganizationRequest
                .builder()
                .name(organization.getName())
                .vatNumber(StringUtils.EMPTY)
                .build();
        mockMvc
                .perform(
                        put("/organization/" + organization.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void post_null_name_should_return_bad_request() throws Exception {
        Organization organization = buildTestOrganization();
        CreateOrganizationRequest request = CreateOrganizationRequest
                .builder()
                .vatNumber(organization.getVatNumber())
                .build();
        mockMvc
                .perform(
                        post("/organization")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void post_invalid_size_name_should_return_bad_request() throws Exception {
        Organization organization = buildTestOrganization();
        CreateOrganizationRequest request = CreateOrganizationRequest
                .builder()
                .name(StringUtils.EMPTY)
                .vatNumber(organization.getVatNumber())
                .build();
        mockMvc
                .perform(
                        post("/organization")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void put_null_name_should_return_bad_request() throws Exception {
        Organization organization = organizationRepository.save(buildTestOrganization());
        UpdateOrganizationRequest request = UpdateOrganizationRequest
                .builder()
                .vatNumber(organization.getVatNumber())
                .build();
        mockMvc
                .perform(
                        put("/organization/" + organization.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void put_invalid_size_name_should_return_bad_request() throws Exception {
        Organization organization = organizationRepository.save(buildTestOrganization());
        UpdateOrganizationRequest request = UpdateOrganizationRequest
                .builder()
                .name(StringUtils.EMPTY)
                .vatNumber(organization.getVatNumber())
                .build();
        mockMvc
                .perform(
                        put("/organization/" + organization.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Override
    @WithMockUser
    public void get_no_entities_in_table_should_return_empty_list() throws Exception {
        mockMvc
                .perform(get("/organization"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @Override
    @WithMockUser
    public void get_one_entity_in_table_should_return_one_element_in_list() throws Exception {
        Organization organization = organizationRepository.save(buildTestOrganization());
        mockMvc
                .perform(get("/organization"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", equalTo((int) organization.getId())))
                .andExpect(jsonPath("$[0].name", equalTo(organization.getName())))
                .andExpect(jsonPath("$[0].vatNumber", equalTo(organization.getVatNumber())))
                .andExpect(jsonPath("$[0].addedAt", notNullValue()))
                .andExpect(jsonPath("$[0].applications", hasSize(0)));
    }

    @Test
    @Override
    @WithMockUser
    public void get_existing_entity_should_pass() throws Exception {
        Organization organization = organizationRepository.save(buildTestOrganization());
        mockMvc
                .perform(
                        get("/organization/" + organization.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo((int) organization.getId())))
                .andExpect(jsonPath("$.name", equalTo(organization.getName())))
                .andExpect(jsonPath("$.vatNumber", equalTo(organization.getVatNumber())))
                .andExpect(jsonPath("$.addedAt", notNullValue()))
                .andExpect(jsonPath("$.applications", hasSize(0)));
    }

    @Test
    @Override
    @WithMockUser
    public void get_not_existing_entity_should_return_not_found() throws Exception {
        mockMvc
                .perform(
                        get("/organization/" + 0))
                .andExpect(status().isNotFound());
    }

    @Test
    @Override
    @WithMockUser
    public void post_valid_entity_should_pass() throws Exception {
        Organization organization = buildTestOrganization();
        CreateOrganizationRequest request = CreateOrganizationRequest
                .builder()
                .name(organization.getName())
                .vatNumber(organization.getVatNumber())
                .build();
        mockMvc
                .perform(
                        post("/organization")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("location"))
                .andExpect(header().string("location", new MatchesPattern(Pattern.compile("^http://.+/organization/\\d+$"))));
    }

    @Test
    @Override
    @WithMockUser
    public void put_valid_entity_should_pass() throws Exception {
        Organization organization = organizationRepository.save(buildTestOrganization());
        UpdateOrganizationRequest request = UpdateOrganizationRequest
                .builder()
                .name(StringUtils.reverse(organization.getName()))
                .vatNumber(StringUtils.reverse(organization.getVatNumber()))
                .build();
        mockMvc
                .perform(
                        put("/organization/" + organization.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request)))
                .andExpect(status().isOk());
        Organization updated = organizationRepository.findById(organization.getId()).orElseThrow(EntityNotFoundException::new);
        assertThat(updated.getId()).isEqualTo(organization.getId());
        assertThat(updated.getName()).isEqualTo(request.getName());
        assertThat(updated.getVatNumber()).isEqualTo(request.getVatNumber());
        assertThat(updated.getAddedAt()).isEqualTo(organization.getAddedAt());
    }

    @Test
    @Override
    @WithMockUser
    public void put_not_existing_entity_should_return_not_found() throws Exception {
        Organization organization = organizationRepository.save(buildTestOrganization());
        UpdateOrganizationRequest request = UpdateOrganizationRequest
                .builder()
                .name(StringUtils.reverse(organization.getName()))
                .vatNumber(StringUtils.reverse(organization.getVatNumber()))
                .build();
        mockMvc
                .perform(
                        put("/organization/" + 0)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    @Override
    @WithMockUser
    public void delete_existing_entity_should_pass() throws Exception {
        Organization organization = organizationRepository.save(buildTestOrganization());
        mockMvc
                .perform(
                        delete("/organization/" + organization.getId()))
                .andExpect(status().isOk());
        assertThat(organizationRepository.findById(organization.getId()).isPresent()).isFalse();
    }

    @Test
    @Override
    @WithMockUser
    public void delete_not_existing_entity_should_return_not_found() throws Exception {
        mockMvc
                .perform(
                        delete("/organization/" + 0))
                .andExpect(status().isNotFound());
    }

    @Override
    protected void doCleanup() {
        organizationRepository.deleteAll();
    }
}
