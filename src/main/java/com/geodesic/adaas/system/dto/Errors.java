package com.geodesic.adaas.system.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Errors {
    NOT_FOUND("NOT_FOUND", "Resource not found"),
    ALREADY_EXISTS("ALREADY_EXISTS", "Resource already exists"),
    INVALID_PARAMETERS("INVALID_PARAMETERS", "Invalid input parameters"),
    INVALID_PAYLOAD("INVALID_PAYLOAD", "Invalid request payload"),
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "Internal server error");

    private final String code;
    private final String title;
}
