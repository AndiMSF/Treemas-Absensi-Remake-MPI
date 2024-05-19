package com.treemaswebapi.treemaswebapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.treemaswebapi.treemaswebapi.entity.ReimburseEntity.ReimburseParamEntity;

@Repository
public interface ReimburseParamRepository extends JpaRepository<ReimburseParamEntity, String> {
}