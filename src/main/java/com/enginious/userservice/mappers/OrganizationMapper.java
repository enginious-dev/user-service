package com.enginious.userservice.mappers;

import com.enginious.userservice.model.Organization;
import com.enginious.userservice.service.organization.dto.UpdateOrganizationRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OrganizationMapper {

    void update(UpdateOrganizationRequest source, @MappingTarget Organization destination);
}
