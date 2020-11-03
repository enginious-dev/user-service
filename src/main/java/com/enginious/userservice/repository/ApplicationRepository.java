package com.enginious.userservice.repository;

import com.enginious.userservice.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    Optional<Application> findOneByOrganizzationIdAndId(Long organizzationId, Long applicationId);

    List<Application> findAllByOrganizzationId(Long organizzationId);

    void deleteOneByOrganizzationIdAndId(Long organizzationId, Long applicationId);
}
