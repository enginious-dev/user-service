package com.enginious.userservice.controller;

import com.enginious.userservice.mappers.ApplicationMapper;
import com.enginious.userservice.mappers.OrganizzationMapper;
import com.enginious.userservice.model.Application;
import com.enginious.userservice.model.Organizzation;
import com.enginious.userservice.repository.ApplicationRepository;
import com.enginious.userservice.repository.OrganizzationRepository;
import org.hibernate.secure.internal.JaccPreLoadEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/organizzation/{organizzationId}/application")
public class ApplicationController {

    private final OrganizzationRepository organizzationRepository;
    private final ApplicationRepository applicationRepository;
    private final ApplicationMapper applicationMapper;

    @Autowired
    public ApplicationController(OrganizzationRepository organizzationRepository, ApplicationRepository applicationRepository, ApplicationMapper applicationMapper) {
        this.organizzationRepository = organizzationRepository;
        this.applicationRepository = applicationRepository;
        this.applicationMapper = applicationMapper;
    }

    @GetMapping("/{applicationId}")
    public Application findApplication(@PathVariable Long organizzationId, @PathVariable Long applicationId) {
        return applicationRepository.findOneByOrganizzationIdAndId(organizzationId, applicationId).orElseThrow(EntityNotFoundException::new);
    }

    @GetMapping
    public List<Application> findApplication(@PathVariable Long organizzationId) {
        return applicationRepository.findAllByOrganizzationId(organizzationId);
    }

    @PutMapping("/{applicationId}")
    public void updateOrganizzation(@PathVariable Long organizzationId, @PathVariable Long applicationId, @Valid @RequestBody Application application) {
        Application existing = applicationRepository.findOneByOrganizzationIdAndId(organizzationId, applicationId).orElseThrow(EntityNotFoundException::new);
        applicationMapper.update(application, existing);
        applicationRepository.save(existing);
    }

    @DeleteMapping("/{applicationId}")
    public void deleteOrganizzation(@PathVariable Long organizzationId, @PathVariable Long applicationId) {
        applicationRepository.deleteOneByOrganizzationIdAndId(organizzationId, applicationId);
    }

    @PostMapping()
    public ResponseEntity<?> addOrganizzation(@PathVariable Long organizzationId, @Valid @RequestBody Application application) {
        Organizzation organizzation = organizzationRepository.findById(organizzationId).orElseThrow(EntityNotFoundException::new);
        application.setAddedAt(LocalDateTime.now());
        application.setOrganizzation(organizzation);
        return ResponseEntity.created(
                ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/{applicationId}")
                        .buildAndExpand(
                                applicationRepository
                                        .save(application)
                                        .getId()
                        )
                        .toUri()
        ).build();
    }
}
