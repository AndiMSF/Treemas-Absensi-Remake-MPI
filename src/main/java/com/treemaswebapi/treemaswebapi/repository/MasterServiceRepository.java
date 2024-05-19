package com.treemaswebapi.treemaswebapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.treemaswebapi.treemaswebapi.entity.MasterServiceEntity.MasterServiceEntity;

@Repository
public interface MasterServiceRepository extends JpaRepository<MasterServiceEntity, String> {
}