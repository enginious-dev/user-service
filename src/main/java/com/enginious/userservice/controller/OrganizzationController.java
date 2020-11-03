package com.enginious.userservice.controller;

import com.enginious.userservice.mappers.OrganizzationMapper;
import com.enginious.userservice.model.Organizzation;
import com.enginious.userservice.repository.OrganizzationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/organizzation")
public class OrganizzationController {

    private final OrganizzationRepository organizzationRepository;
    private final OrganizzationMapper organizzationMapper;

    @Autowired
    public OrganizzationController(OrganizzationRepository organizzationRepository, OrganizzationMapper organizzationMapper) {
        this.organizzationRepository = organizzationRepository;
        this.organizzationMapper = organizzationMapper;
    }

    @GetMapping("/{organizzationId}")
    public Organizzation findOrganizzation(@PathVariable Long organizzationId) {
        return organizzationRepository.findById(organizzationId).orElseThrow(EntityNotFoundException::new);
    }

    @GetMapping
    public List<Organizzation> findOrganizzation() {
        return organizzationRepository.findAll();
    }

    @PutMapping("/{organizzationId}")
    public void updateOrganizzation(@PathVariable Long organizzationId, @Valid @RequestBody Organizzation organizzation) {
        Organizzation existing = organizzationRepository.findById(organizzationId).orElseThrow(EntityNotFoundException::new);
        organizzationMapper.update(organizzation, existing);
        organizzationRepository.save(existing);
    }

    @DeleteMapping("/{organizzationId}")
    public void deleteOrganizzation(@PathVariable Long organizzationId) {
        organizzationRepository.deleteById(organizzationId);
    }

    @PostMapping()
    public ResponseEntity<?> addOrganizzation(@Valid @RequestBody Organizzation organizzation) {
        organizzation.setAddedAt(LocalDateTime.now());
        return ResponseEntity.created(
                ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/{organizzationId}")
                        .buildAndExpand(
                                organizzationRepository
                                        .save(organizzation)
                                        .getId()
                        )
                        .toUri()
        ).build();
    }
}
