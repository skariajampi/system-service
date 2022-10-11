package com.geodesic.adaas.system.service;

import com.geodesic.adaas.system.dto.CreateUpdateSystemRequest;
import com.geodesic.adaas.system.dto.SystemResponse;
import com.geodesic.adaas.system.entity.SystemEntity;
import com.geodesic.adaas.system.exception.RecordNotFoundException;
import com.geodesic.adaas.system.repository.SystemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdaasSystemServiceImpl implements AdaasSystemService {

    private final SystemRepository systemRepository;

    public AdaasSystemServiceImpl(SystemRepository systemRepository) {
        this.systemRepository = systemRepository;
    }

    public SystemResponse save(CreateUpdateSystemRequest createUpdateSystemRequest) {

        SystemEntity entity = createEntity(createUpdateSystemRequest);
        SystemEntity savedEntity = systemRepository.save(entity);
        return createResponse(savedEntity);
    }

    @Override
    public SystemResponse update(String code, CreateUpdateSystemRequest createUpdateSystemRequest) throws RecordNotFoundException {

        Optional<SystemEntity> entityOptional = systemRepository.findByCode(code);
        if (entityOptional.isPresent()) {
            SystemEntity entity = entityOptional.get();
            entity.setCode(createUpdateSystemRequest.getCode());
            entity.setName(createUpdateSystemRequest.getName());
            entity.setDescription(createUpdateSystemRequest.getDescription());
            systemRepository.save(entity);
            return createResponse(entity);
        } else {
            throw new RecordNotFoundException(code + " not found");
        }
    }

    public SystemResponse getAdaasSystem(String code) throws RecordNotFoundException {

        Optional<SystemEntity> optionalSystemEntity = systemRepository.findByCode(code);
        return optionalSystemEntity.map(this::createResponse).orElseThrow(() -> new RecordNotFoundException(code + " not found"));
    }

    @Override
    public List<SystemResponse> getAdaasSystems() {

        return systemRepository.findAll().stream().map(this::createResponse).toList();
    }

    @Override
    public void deleteByCode(String code) throws RecordNotFoundException {
        Optional<SystemEntity> entityOptional = systemRepository.findByCode(code);
        if (entityOptional.isPresent()) {
            systemRepository.deleteById(entityOptional.get().getId());
        } else {
            throw new RecordNotFoundException(code + " not found");
        }
    }

    private SystemEntity createEntity(CreateUpdateSystemRequest request) {
        return SystemEntity
                .builder()
                .code(request.getCode())
                .description(request.getDescription())
                .name(request.getName())
                .build();
    }

    private SystemResponse createResponse(SystemEntity entity) {

        return SystemResponse
                .builder()
                .code(entity.getCode())
                .description(entity.getDescription())
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }
}
