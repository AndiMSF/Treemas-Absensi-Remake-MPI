package com.treemaswebapi.treemaswebapi.repository;




import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.treemaswebapi.treemaswebapi.entity.GeneralEntity.GeneralParamApprovalEntity;
import com.treemaswebapi.treemaswebapi.entity.ProjectEntity.ProjectEntity;

@Repository
public interface GeneralParamApprovalRepository extends JpaRepository<GeneralParamApprovalEntity, String> {

    Long countByIsApproveIsNull();

    
}
