package com.geodesic.adaas.system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Builder
@Getter
public class SystemResponse {

  private UUID id;

  private String code;

  private String name;

  private String description;
}
