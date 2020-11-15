package com.enginious.userservice.service.organization.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class CreateOrganizationRequest {

    @NotNull
    @Size(min = 2)
    @Column(name = "vatNumber", nullable = false)
    private String vatNumber;

    @NotNull
    @Size(min = 2)
    @Column(name = "name", nullable = false)
    private String name;

    @Builder
    public CreateOrganizationRequest(@NotNull @Size(min = 2) String vatNumber, @NotNull @Size(min = 2) String name) {
        this.vatNumber = vatNumber;
        this.name = name;
    }
}
