package com.enginious.userservice.controller;

import com.enginious.userservice.model.Application;
import com.enginious.userservice.model.Organization;
import com.enginious.userservice.model.enums.Role;
import com.enginious.userservice.repository.ApplicationRepository;
import com.enginious.userservice.repository.OrganizationRepository;
import com.enginious.userservice.service.application.dto.CreateApplicationRequest;
import com.enginious.userservice.service.application.dto.UpdateApplicationRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.text.MatchesPattern;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
public class ApplicationControllerTest extends ControllerTest {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    private Application buildTestApplication(Organization organization) {
        return Application
                .builder()
                .name("UserService")
                .uuid(UUID.randomUUID().toString())
                .secret("secret")
                .role(Role.ADMIN)
                .organization(organization)
                .build();
    }

    private Organization buildTestOrganization() {
        return Organization
                .builder()
                .name("enginious")
                .vatNumber("1234567")
                .build();
    }

    @Test
    @Override
    @WithMockUser
    public void get_no_entities_in_table_should_return_empty_list() throws Exception {
        mockMvc
                .perform(get("/organization/1/application"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @Override
    @WithMockUser
    public void get_one_entity_in_table_should_return_one_element_in_list() throws Exception {
        Application application = applicationRepository.save(buildTestApplication(organizationRepository.save(buildTestOrganization())));
        mockMvc
                .perform(get("/organization/" + application.getOrganization().getId() + "/application"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", equalTo((int) application.getId())))
                .andExpect(jsonPath("$[0].name", equalTo(application.getName())))
                .andExpect(jsonPath("$[0].uuid", equalTo(application.getUuid())))
                .andExpect(jsonPath("$[0].secret", equalTo(application.getSecret())))
                .andExpect(jsonPath("$[0].role", equalTo(application.getRole().name())));
    }

    @Test
    @Override
    @WithMockUser
    public void get_existing_entity_should_pass() throws Exception {
        Application application = applicationRepository.save(buildTestApplication(organizationRepository.save(buildTestOrganization())));
        mockMvc
                .perform(get("/organization/" + application.getOrganization().getId() + "/application/" + application.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo((int) application.getId())))
                .andExpect(jsonPath("$.name", equalTo(application.getName())))
                .andExpect(jsonPath("$.uuid", equalTo(application.getUuid())))
                .andExpect(jsonPath("$.secret", equalTo(application.getSecret())))
                .andExpect(jsonPath("$.role", equalTo(application.getRole().name())))
                .andExpect(jsonPath("$.organization").doesNotExist());
    }

    @Test
    @Override
    @WithMockUser
    public void get_not_existing_entity_should_return_not_found() throws Exception {
        mockMvc
                .perform(get("/organization/0/application/0"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Override
    @WithMockUser
    public void post_valid_entity_should_pass() throws Exception {
        Organization organization = organizationRepository.save(buildTestOrganization());
        Application application = buildTestApplication(organization);
        CreateApplicationRequest request = CreateApplicationRequest
                .builder()
                .name(application.getName())
                .role(application.getRole())
                .build();
        mockMvc
                .perform(
                        post("/organization/" + organization.getId() + "/application")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("location"))
                .andExpect(header().string("location", new MatchesPattern(Pattern.compile("^http://.+/application/\\d+$"))));
    }

    @Test
    @Override
    @WithMockUser
    public void put_valid_entity_should_pass() throws Exception {
        Application application = applicationRepository.save(buildTestApplication(organizationRepository.save(buildTestOrganization())));
        UpdateApplicationRequest request = UpdateApplicationRequest
                .builder()
                .name(StringUtils.reverse(application.getName()))
                .role(Role.USER)
                .build();
        mockMvc
                .perform(
                        put("/organization/" + application.getOrganization().getId() + "/application/" + application.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request)))
                .andExpect(status().isOk());

        Application updated = applicationRepository.findById(application.getId()).orElseThrow(EntityNotFoundException::new);
        assertThat(updated.getName()).isEqualTo(request.getName());
        assertThat(updated.getRole()).isEqualTo(request.getRole());
    }

    @Test
    @Override
    @WithMockUser
    public void put_not_existing_entity_should_return_not_found() throws Exception {
        Application application = applicationRepository.save(buildTestApplication(organizationRepository.save(buildTestOrganization())));
        UpdateApplicationRequest request = UpdateApplicationRequest
                .builder()
                .name(StringUtils.reverse(application.getName()))
                .role(Role.USER)
                .build();
        mockMvc
                .perform(
                        put("/organization/" + application.getOrganization().getId() + "/application/" + 0)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    @Override
    @WithMockUser
    public void delete_existing_entity_should_pass() throws Exception {
        Application application = applicationRepository.save(buildTestApplication(organizationRepository.save(buildTestOrganization())));
        mockMvc
                .perform(
                        delete("/organization/" + application.getOrganization().getId() + "/application/" + application.getId()))
                .andExpect(status().isOk());
        assertThat(organizationRepository.findById(application.getId()).isPresent()).isFalse();
    }

    @Test
    @Override
    @WithMockUser
    public void delete_not_existing_entity_should_return_not_found() throws Exception {
        mockMvc
                .perform(
                        delete("/organization/" + 0 + "/application/" + 0))
                .andExpect(status().isNotFound());
    }

    @Override
    protected void doCleanup() {
        applicationRepository.deleteAll();
        organizationRepository.deleteAll();
    }
}
