package com.enginious.userservice.service.application.dto;

import com.enginious.userservice.model.enums.Role;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class UpdateApplicationRequest {

    @NotNull
    @Size(min = 2)
    private String name;

    @NotNull
    private Role role;

    @Builder
    public UpdateApplicationRequest(@NotNull @Size(min = 2) String name, @NotNull Role role) {
        this.name = name;
        this.role = role;
    }
}
