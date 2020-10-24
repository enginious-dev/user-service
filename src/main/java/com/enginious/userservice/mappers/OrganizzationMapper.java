package com.enginious.userservice.mappers;

import com.enginious.userservice.model.Organizzation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OrganizzationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "addedAt", ignore = true)
    void update(Organizzation source, @MappingTarget Organizzation destination);
}
