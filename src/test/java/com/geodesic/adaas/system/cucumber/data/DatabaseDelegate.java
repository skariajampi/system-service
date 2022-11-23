package com.geodesic.adaas.system.cucumber.data;

import com.geodesic.adaas.system.entity.SystemEntity;
import com.geodesic.adaas.system.repository.SystemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class DatabaseDelegate {

  @Autowired private SystemRepository systemRepository;

  @Transactional
  public SystemEntity createSystem(String code, String name, String description) {

    SystemEntity systemEntity = new SystemEntity();
    systemEntity.setCode(code);
    systemEntity.setName(name);
    systemEntity.setDescription(description);

    return systemRepository.save(systemEntity);
  }

  public List<SystemEntity> getAllSystems() {
    return systemRepository.findAll();
  }

  public void clear() {
    systemRepository.deleteAll();
  }
}
