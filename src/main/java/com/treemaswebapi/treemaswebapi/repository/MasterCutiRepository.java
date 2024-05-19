package com.treemaswebapi.treemaswebapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.treemaswebapi.treemaswebapi.entity.CutiEntity.MasterCutiEntity;

@Repository
public interface MasterCutiRepository extends JpaRepository<MasterCutiEntity, String> {
    
}
