package com.geodesic.adaas.system.repository;

import com.geodesic.adaas.system.entity.SystemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SystemRepository extends JpaRepository<SystemEntity, UUID> {

    Optional<SystemEntity> findByCode(String code);
}