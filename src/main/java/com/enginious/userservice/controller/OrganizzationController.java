package com.enginious.userservice.controller;

import com.enginious.userservice.model.Organizzation;
import com.enginious.userservice.repository.OrganizzationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping(path = "/organizzation")
public class OrganizzationController {

    @Autowired
    private OrganizzationRepository organizzationRepository;

    @GetMapping("/{organizzationId}")
    public Organizzation findOrganizzation(@PathVariable Long organizzationId) {
        return organizzationRepository.findById(organizzationId).orElseThrow(EntityNotFoundException::new);
    }

    @GetMapping
    public List<Organizzation> findOrganizzation() {
        return organizzationRepository.findAll();
    }
}
