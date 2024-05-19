package com.treemaswebapi.treemaswebapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.treemaswebapi.treemaswebapi.entity.GeneralEntity.IncludeTableListEntity;

@Repository
public interface IncludeTableListRepository extends JpaRepository<IncludeTableListEntity, String> {
}