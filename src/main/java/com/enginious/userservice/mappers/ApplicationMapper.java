package com.enginious.userservice.mappers;

import com.enginious.userservice.model.Application;
import com.enginious.userservice.service.application.dto.UpdateApplicationRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ApplicationMapper {

    void update(UpdateApplicationRequest source, @MappingTarget Application destination);
}
