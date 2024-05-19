
package com.treemaswebapi.treemaswebapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.treemaswebapi.treemaswebapi.entity.AbsenEntity.AbsenAppEntity;
import com.treemaswebapi.treemaswebapi.entity.ProjectEntity.ProjectEntity;

import lombok.RequiredArgsConstructor;

@Repository
public interface AbsenAppRepository extends JpaRepository<AbsenAppEntity, Long> {
    
    List<AbsenAppEntity> findAll();

    @Query
    ("SELECT a FROM AbsenAppEntity a WHERE a.projectId = :projectId")
    List<AbsenAppEntity> findAllByProjectId(@Param("projectId") ProjectEntity projectId);

    
    @Query("SELECT a FROM AbsenAppEntity a WHERE a.projectId = :projectId AND a.isLibur = :isLibur")
    List<AbsenAppEntity> findAllByProjectIdAndIsLibur(
            @Param("projectId") ProjectEntity projectId,
            @Param("isLibur") String isLibur
    );

    @Query("SELECT a FROM AbsenAppEntity a WHERE a.projectId = :projectId AND a.isLembur = :isLembur")
    List<AbsenAppEntity> findAllByProjectIdAndIsLembur(
            @Param("projectId") ProjectEntity projectId,
            @Param("isLembur") String isLembur
    );

    Long countByProjectId(ProjectEntity projectId);

    Long countByProjectIdAndIsLibur(ProjectEntity projectId, String string);

    Long countByProjectIdAndIsLembur(ProjectEntity projectId, String string);

    List<AbsenAppEntity> findAllByProjectIdAndIsLiburAndIsApproveIsNull(ProjectEntity projectId, String string);

    List<AbsenAppEntity> findAllByProjectIdAndIsLemburAndIsApproveIsNull(ProjectEntity projectId, String string);

List<AbsenAppEntity> findByIsApproveIsNull();

Long countByIsApproveIsNull();

Long countByProjectIdAndIsLemburAndIsApproveIsNull(ProjectEntity projectId, String string);

Long countByProjectIdAndIsLiburAndIsApproveIsNull(ProjectEntity projectId, String string);
}