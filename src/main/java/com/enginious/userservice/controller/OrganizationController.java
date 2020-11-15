package com.enginious.userservice.controller;

import com.enginious.userservice.model.Organization;
import com.enginious.userservice.service.organization.OrganizationService;
import com.enginious.userservice.service.organization.dto.CreateOrganizationRequest;
import com.enginious.userservice.service.organization.dto.UpdateOrganizationRequest;
import com.enginious.userservice.service.organization.exceptions.CreateOrganizationException;
import com.enginious.userservice.service.organization.exceptions.DeleteOrganizationException;
import com.enginious.userservice.service.organization.exceptions.ReadOrganizationException;
import com.enginious.userservice.service.organization.exceptions.UpdateOrganizationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/organization")
public class OrganizationController extends BaseController {

    private final OrganizationService organizationService;

    @Autowired
    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @PostMapping
    public ResponseEntity<?> createOrganization(@Valid @RequestBody CreateOrganizationRequest request) throws Throwable {
        try {
            log.info(executing());
            return ResponseEntity.created(
                    ServletUriComponentsBuilder
                            .fromCurrentRequest()
                            .path("/{organizationId}")
                            .buildAndExpand(
                                    organizationService
                                            .createOrganization(request)
                                            .getId()
                            )
                            .toUri()
            ).build();
        } catch (CreateOrganizationException e) {
            log.error(error(), e);
            throw e.getCause();
        }
    }

    @GetMapping("/{organizationId}")
    public Organization readOrganization(@PathVariable Long organizationId) throws Throwable {
        try {
            log.info(executing());
            return organizationService.readOrganization(organizationId);
        } catch (ReadOrganizationException e) {
            log.error(error(), e);
            throw e.getCause();
        }
    }

    @GetMapping
    public List<Organization> readOrganization() throws Throwable {
        try {
            return organizationService.readOrganization();
        } catch (ReadOrganizationException e) {
            log.error(error(), e);
            throw e.getCause();
        }
    }

    @PutMapping("/{organizationId}")
    public void updateOrganization(@PathVariable Long organizationId, @Valid @RequestBody UpdateOrganizationRequest request) throws Throwable {
        try {
            log.info(executing());
            organizationService.updateOrganization(request, organizationId);
        } catch (UpdateOrganizationException e) {
            log.error(error(), e);
            throw e.getCause();
        }
    }

    @DeleteMapping("/{organizationId}")
    public void deleteOrganization(@PathVariable Long organizationId) throws Throwable {
        try {
            log.info(executing());
            organizationService.deleteOrganization(organizationId);
        } catch (DeleteOrganizationException e) {
            log.error(error(), e);
            throw e.getCause();
        }
    }
}
