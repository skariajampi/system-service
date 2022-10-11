package com.geodesic.adaas.system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Builder
@Getter
public class CreateUpdateSystemRequest {

    @NotBlank
    private String code;

    @NotBlank
    private String name;

    @NotBlank
    private String description;
}
