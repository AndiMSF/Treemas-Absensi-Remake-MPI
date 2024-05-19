package com.treemaswebapi.treemaswebapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.treemaswebapi.treemaswebapi.entity.TempQueriesEntity.TempQueriesEntity;

@Repository
public interface TempQueriesRepository extends JpaRepository<TempQueriesEntity, Long> {
}