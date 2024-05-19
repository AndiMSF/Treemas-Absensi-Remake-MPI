package com.treemaswebapi.treemaswebapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.treemaswebapi.treemaswebapi.entity.KaryawanEntity.KaryawanImageEntity;

@Repository
public interface KaryawanImageRepository extends JpaRepository<KaryawanImageEntity, String> {
    Optional<KaryawanImageEntity> findByNik(String nik);
}
