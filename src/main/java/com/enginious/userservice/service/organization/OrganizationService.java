package com.enginious.userservice.service.organization;

import com.enginious.userservice.mappers.OrganizationMapper;
import com.enginious.userservice.model.Organization;
import com.enginious.userservice.repository.OrganizationRepository;
import com.enginious.userservice.service.organization.dto.CreateOrganizationRequest;
import com.enginious.userservice.service.organization.dto.UpdateOrganizationRequest;
import com.enginious.userservice.service.organization.exceptions.CreateOrganizationException;
import com.enginious.userservice.service.organization.exceptions.DeleteOrganizationException;
import com.enginious.userservice.service.organization.exceptions.ReadOrganizationException;
import com.enginious.userservice.service.organization.exceptions.UpdateOrganizationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@Service
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final OrganizationMapper organizationMapper;

    @Autowired
    public OrganizationService(OrganizationRepository organizationRepository, OrganizationMapper organizationMapper) {
        this.organizationRepository = organizationRepository;
        this.organizationMapper = organizationMapper;
    }

    @Transactional(rollbackOn = CreateOrganizationException.class)
    public Organization createOrganization(@Valid CreateOrganizationRequest request) throws CreateOrganizationException {
        try {
            log.trace(String.format("creating organization with name [%s] and vatNumber [%s]", request.getName(), request.getVatNumber()));
            Organization organization = organizationRepository.save(
                    Organization
                            .builder()
                            .name(request.getName())
                            .vatNumber(request.getVatNumber())
                            .build()
            );
            log.trace(String.format("created organization with id [%09d]", organization.getId()));
            return organization;
        } catch (Exception e) {
            String msg = String.format("failed to create organization with name [%s] and vatNumber [%s]", request.getName(), request.getVatNumber());
            log.error(msg, e);
            throw new CreateOrganizationException(msg, e);
        }
    }

    @Transactional(rollbackOn = ReadOrganizationException.class)
    public Organization readOrganization(Long organizationId) throws ReadOrganizationException {
        try {
            log.trace(String.format("reading organization with id [%09d]", organizationId));
            Organization organization = organizationRepository.findById(organizationId).orElseThrow(EntityNotFoundException::new);
            log.trace(String.format("read organization with id [%09d]", organizationId));
            return organization;
        } catch (Exception e) {
            String msg = String.format("failed to read organization with id [%09d]", organizationId);
            log.error(msg, e);
            throw new ReadOrganizationException(msg, e);
        }
    }

    @Transactional(rollbackOn = ReadOrganizationException.class)
    public List<Organization> readOrganization() throws ReadOrganizationException {
        try {
            log.trace("reading organizations");
            List<Organization> applications = organizationRepository.findAll();
            log.trace("read organizations");
            return applications;
        } catch (Exception e) {
            String msg = "failed to read organizations";
            log.error(msg, e);
            throw new ReadOrganizationException(msg, e);
        }
    }

    @Transactional(rollbackOn = UpdateOrganizationException.class)
    public Organization updateOrganization(@Valid UpdateOrganizationRequest request, Long organizationId) throws UpdateOrganizationException {
        try {
            log.trace(String.format("updating organization with id [%09d]", organizationId));
            Organization existing = organizationRepository.findById(organizationId).orElseThrow(EntityNotFoundException::new);
            organizationMapper.update(request, existing);
            Organization updated = organizationRepository.save(existing);
            log.trace(String.format("updated organization with id [%09d]", updated.getId()));
            return updated;
        } catch (Exception e) {
            String msg = String.format("failed to update organization with id [%09d]", organizationId);
            log.error(msg, e);
            throw new UpdateOrganizationException(msg, e);
        }
    }

    @Transactional(rollbackOn = DeleteOrganizationException.class)
    public void deleteOrganization(Long organizationId) throws DeleteOrganizationException {
        try {
            log.trace(String.format("deleting organization with id [%09d]", organizationId));
            organizationRepository.deleteById(organizationId);
            log.trace(String.format("deleted organization with id [%09d]", organizationId));
        } catch (Exception e) {
            String msg = String.format("failed to delete organization with id [%09d]", organizationId);
            log.error(msg, e);
            throw new DeleteOrganizationException(msg, e);
        }
    }
}
