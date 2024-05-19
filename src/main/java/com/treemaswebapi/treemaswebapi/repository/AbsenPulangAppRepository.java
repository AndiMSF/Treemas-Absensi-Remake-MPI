package com.treemaswebapi.treemaswebapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.treemaswebapi.treemaswebapi.entity.AbsenEntity.AbsenPulangAppEntity;
import com.treemaswebapi.treemaswebapi.entity.ProjectEntity.ProjectEntity;

@Repository
public interface AbsenPulangAppRepository extends JpaRepository<AbsenPulangAppEntity, Long> {

    List<AbsenPulangAppEntity> findAllByProjectId(ProjectEntity projectId);

    List<AbsenPulangAppEntity> findAllByProjectIdAndIsApproveIsNull(ProjectEntity projectId);

    List<AbsenPulangAppEntity> findByIsApproveIsNull();

    Long countByIsApproveIsNull();

    Long countByProjectIdAndIsApproveIsNull(ProjectEntity projectId);
}

