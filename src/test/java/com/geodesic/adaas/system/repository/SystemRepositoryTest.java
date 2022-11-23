package com.geodesic.adaas.system.repository;

import com.geodesic.adaas.system.entity.SystemEntity;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class SystemRepositoryTest {

  @Container
  private static final PostgreSQLContainer<?> postgres =
      new PostgreSQLContainer<>(DockerImageName.parse("postgres").withTag("12.3"))
          .withPassword("postgres");

  @DynamicPropertySource
  static void registerDynamicProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
  }

  @Autowired private SystemRepository systemRepository;

  @Test
  void getAllSystems_shouldReturnAllSystems() {

    List<SystemEntity> systemEntities = systemRepository.findAll();

    assertThat(systemEntities).hasSize(4);
    assertThat(systemEntities).extracting("code").contains("JSA", "MA", "AFN", "CN");
  }

  @Test
  void getSystemByCode_shouldReturnASystem() {

    Optional<SystemEntity> systemEntity = systemRepository.findByCode("AFN");

    assertThat(systemEntity.isPresent());
    assertThat(systemEntity.get().getCode()).isEqualTo("AFN");
  }

  @Test
  @Transactional
  void saveASystem_shouldReturnASystem() {

    SystemEntity systemEntityToBeSaved = getSystemEntity();

    systemRepository.deleteAll();

    SystemEntity systemEntity = systemRepository.save(systemEntityToBeSaved);

    assertThat(systemEntity.getCode()).isEqualTo("JSA2");

    Optional<SystemEntity> savedSystemEntity = systemRepository.findByCode("JSA2");

    assertThat(savedSystemEntity.isPresent());
    assertThat(savedSystemEntity.get().getCode()).isEqualTo("JSA2");
  }

  @NotNull
  private SystemEntity getSystemEntity() {
    SystemEntity adaasSystem = new SystemEntity();
    adaasSystem.setId(UUID.randomUUID());
    adaasSystem.setCode("JSA2");
    adaasSystem.setName("Job Seekers Allowance2");
    adaasSystem.setDescription("Description: Job Seekers Allowance2");
    return adaasSystem;
  }
}
