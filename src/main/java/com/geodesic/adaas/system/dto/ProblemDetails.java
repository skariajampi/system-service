package com.geodesic.adaas.system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class ProblemDetails {

  private final String code;

  private final String title;

  private final String message;
}
