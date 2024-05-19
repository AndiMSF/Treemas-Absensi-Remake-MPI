package com.treemaswebapi.treemaswebapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.treemaswebapi.treemaswebapi.entity.JabatanEntity.JabatanEntity;


@Repository
public interface JabatanRepository extends JpaRepository<JabatanEntity, String> {
}