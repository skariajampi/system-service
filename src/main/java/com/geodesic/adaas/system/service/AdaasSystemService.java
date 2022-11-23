package com.geodesic.adaas.system.service;

import com.geodesic.adaas.system.dto.CreateUpdateSystemRequest;
import com.geodesic.adaas.system.dto.SystemResponse;
import com.geodesic.adaas.system.exception.RecordNotFoundException;

import java.util.List;

public interface AdaasSystemService {

  SystemResponse save(CreateUpdateSystemRequest createUpdateSystemRequest);

  SystemResponse update(String code, CreateUpdateSystemRequest createUpdateSystemRequest)
      throws RecordNotFoundException;

  SystemResponse getAdaasSystem(String code) throws RecordNotFoundException;

  List<SystemResponse> getAdaasSystems();

  void deleteByCode(String code) throws RecordNotFoundException;
}
