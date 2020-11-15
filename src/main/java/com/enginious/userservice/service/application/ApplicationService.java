package com.enginious.userservice.service.application;

import com.enginious.userservice.mappers.ApplicationMapper;
import com.enginious.userservice.model.Application;
import com.enginious.userservice.model.Organization;
import com.enginious.userservice.repository.ApplicationRepository;
import com.enginious.userservice.repository.OrganizationRepository;
import com.enginious.userservice.service.application.dto.CreateApplicationRequest;
import com.enginious.userservice.service.application.dto.UpdateApplicationRequest;
import com.enginious.userservice.service.application.exceptions.CreateApplicationException;
import com.enginious.userservice.service.application.exceptions.DeleteApplicationException;
import com.enginious.userservice.service.application.exceptions.ReadApplicationException;
import com.enginious.userservice.service.application.exceptions.UpdateApplicationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ApplicationService {

    private static final int APPLICATION_SECRET_LENGTH = 16;

    private final OrganizationRepository organizationRepository;
    private final ApplicationRepository applicationRepository;
    private final ApplicationMapper applicationMapper;

    @Autowired
    public ApplicationService(ApplicationRepository applicationRepository, OrganizationRepository organizationRepository, ApplicationMapper applicationMapper) {
        this.organizationRepository = organizationRepository;
        this.applicationRepository = applicationRepository;
        this.applicationMapper = applicationMapper;
    }

    @Transactional(rollbackOn = CreateApplicationException.class)
    public Application createApplication(@Valid CreateApplicationRequest request, Long organizationId) throws CreateApplicationException {
        try {
            log.trace(String.format("creating application with name [%s], role [%s] for organization with id [%09d]", request.getName(), request.getRole().name(), organizationId));
            Organization organization = organizationRepository.findById(organizationId).orElseThrow(EntityNotFoundException::new);
            Application application = applicationRepository.save(
                    Application
                            .builder()
                            .name(request.getName())
                            .uuid(UUID.randomUUID().toString())
                            .secret(RandomStringUtils.randomAlphanumeric(APPLICATION_SECRET_LENGTH))
                            .role(request.getRole())
                            .organization(organization)
                            .build()
            );
            log.trace(String.format("created application with id [%09d]", application.getId()));
            return application;
        } catch (Exception e) {
            String msg = String.format("failed to create application with name [%s], role [%s] for organization with id [%09d]", request.getName(), request.getRole().name(), organizationId);
            log.error(msg, e);
            throw new CreateApplicationException(msg, e);
        }
    }

    @Transactional(rollbackOn = ReadApplicationException.class)
    public Application readApplication(Long applicationId, Long organizationId) throws ReadApplicationException {
        try {
            log.trace(String.format("reading application with id [%09d] for organization with id [%09d]", applicationId, organizationId));
            Application application = applicationRepository.findOneByOrganizationIdAndId(organizationId, applicationId).orElseThrow(EntityNotFoundException::new);
            log.trace(String.format("read application with id [%09d] for organization with id [%09d]", applicationId, organizationId));
            return application;
        } catch (Exception e) {
            String msg = String.format("failed to read application with id [%09d] for organization with id [%09d]", applicationId, organizationId);
            log.error(msg, e);
            throw new ReadApplicationException(msg, e);
        }
    }

    @Transactional(rollbackOn = ReadApplicationException.class)
    public List<Application> readApplication(Long organizationId) throws ReadApplicationException {
        try {
            log.trace(String.format("reading applications for organization with id [%09d]", organizationId));
            List<Application> applications = applicationRepository.findAllByOrganizationId(organizationId);
            log.trace(String.format("read applications for organization with id [%09d]", organizationId));
            return applications;
        } catch (Exception e) {
            String msg = String.format("failed to read application for organization with id [%09d]", organizationId);
            log.error(msg, e);
            throw new ReadApplicationException(msg, e);
        }
    }

    @Transactional(rollbackOn = UpdateApplicationException.class)
    public Application updateApplication(@Valid UpdateApplicationRequest request, Long applicationId, Long organizationId) throws UpdateApplicationException {
        try {
            log.trace(String.format("updating application with id [%09d] for organization with id [%09d]", applicationId, organizationId));
            Application existing = applicationRepository.findOneByOrganizationIdAndId(organizationId, applicationId).orElseThrow(EntityNotFoundException::new);
            applicationMapper.update(request, existing);
            Application updated = applicationRepository.save(existing);
            log.trace(String.format("updated application with id [%09d] for organization with id [%09d]", applicationId, organizationId));
            return updated;
        } catch (Exception e) {
            String msg = String.format("failed to update application with id [%09d] for organization with id [%09d]", applicationId, organizationId);
            log.error(msg, e);
            throw new UpdateApplicationException(msg, e);
        }
    }

    @Transactional(rollbackOn = DeleteApplicationException.class)
    public void deleteApplication(Long applicationId, Long organizationId) throws DeleteApplicationException {
        try {
            log.trace(String.format("deleting application with id [%09d] for organization with id [%09d]", applicationId, organizationId));
            Application application = applicationRepository.findOneByOrganizationIdAndId(organizationId, applicationId).orElseThrow(EntityNotFoundException::new);
            applicationRepository.delete(application);
            log.trace(String.format("deleted application with id [%09d] for organization with id [%09d]", applicationId, organizationId));
        } catch (Exception e) {
            String msg = String.format("failed to delete application with id [%09d] for organization with id [%09d]", applicationId, organizationId);
            log.error(msg, e);
            throw new DeleteApplicationException(msg, e);
        }
    }
}
