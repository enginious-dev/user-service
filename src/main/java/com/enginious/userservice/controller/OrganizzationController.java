package com.enginious.userservice.controller;

import com.enginious.userservice.mappers.OrganizzationMapper;
import com.enginious.userservice.model.Organizzation;
import com.enginious.userservice.repository.OrganizzationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(path = "/organizzation")
public class OrganizzationController {

    @Autowired
    private OrganizzationRepository organizzationRepository;

    @Autowired
    private OrganizzationMapper organizzationMapper;

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

    @PostMapping
    public ResponseEntity<?> addOrganizzation(@Valid @RequestBody Organizzation organizzation) {
        if (Objects.nonNull(organizzation.getId())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        organizzation.setAddedAt(new Date());
        return ResponseEntity.created(
                ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/{organizzationId}")
                        .buildAndExpand(
                                organizzationRepository
                                        .save(organizzation)
                        )
                        .toUri()
        ).build();
    }
}
