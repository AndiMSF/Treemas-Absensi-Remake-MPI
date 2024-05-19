package com.treemaswebapi.treemaswebapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.treemaswebapi.treemaswebapi.entity.CutiEntity.CutiImageAppEntity;

@Repository
public interface CutiImageAppRepository extends JpaRepository<CutiImageAppEntity, Long> {
    Optional<CutiImageAppEntity> findByNik(String nik);
}