package com.treemaswebapi.treemaswebapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.treemaswebapi.treemaswebapi.entity.BeritaEntity.BeritaEntity;


@Repository
public interface BeritaRepository extends JpaRepository<BeritaEntity, Integer> {
}
