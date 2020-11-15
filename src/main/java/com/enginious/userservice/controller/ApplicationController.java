package com.enginious.userservice.controller;

import com.enginious.userservice.model.Application;
import com.enginious.userservice.service.application.ApplicationService;
import com.enginious.userservice.service.application.dto.CreateApplicationRequest;
import com.enginious.userservice.service.application.dto.UpdateApplicationRequest;
import com.enginious.userservice.service.application.exceptions.CreateApplicationException;
import com.enginious.userservice.service.application.exceptions.DeleteApplicationException;
import com.enginious.userservice.service.application.exceptions.ReadApplicationException;
import com.enginious.userservice.service.application.exceptions.UpdateApplicationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/organization/{organizationId}/application")
public class ApplicationController extends BaseController {

    private final ApplicationService applicationService;

    @Autowired
    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping
    public ResponseEntity<?> createApplication(@PathVariable Long organizationId, @Valid @RequestBody CreateApplicationRequest request) throws Throwable {
        try {
            log.info(executing());
            return ResponseEntity.created(
                    ServletUriComponentsBuilder
                            .fromCurrentRequest()
                            .path("/{applicationId}")
                            .buildAndExpand(
                                    applicationService
                                            .createApplication(request, organizationId)
                                            .getId()
                            )
                            .toUri()
            ).build();
        } catch (CreateApplicationException e) {
            log.error(error(), e);
            throw e.getCause();
        }
    }

    @GetMapping("/{applicationId}")
    public Application readApplication(@PathVariable Long organizationId, @PathVariable Long applicationId) throws Throwable {
        try {
            log.info(executing());
            return applicationService.readApplication(applicationId, organizationId);
        } catch (ReadApplicationException e) {
            log.error(error(), e);
            throw e.getCause();
        }
    }

    @GetMapping
    public List<Application> readApplication(@PathVariable Long organizationId) throws Throwable {
        try {
            return applicationService.readApplication(organizationId);
        } catch (ReadApplicationException e) {
            log.error(error(), e);
            throw e.getCause();
        }
    }

    @PutMapping("/{applicationId}")
    public void updateApplication(@PathVariable Long organizationId, @PathVariable Long applicationId, @Valid @RequestBody UpdateApplicationRequest request) throws Throwable {
        try {
            log.info(executing());
            applicationService.updateApplication(request, applicationId, organizationId);
        } catch (UpdateApplicationException e) {
            log.error(error(), e);
            throw e.getCause();
        }
    }

    @DeleteMapping("/{applicationId}")
    public void deleteApplication(@PathVariable Long organizationId, @PathVariable Long applicationId) throws Throwable {
        try {
            log.info(executing());
            applicationService.deleteApplication(applicationId, organizationId);
        } catch (DeleteApplicationException e) {
            log.error(error(), e);
            throw e.getCause();
        }
    }
}
