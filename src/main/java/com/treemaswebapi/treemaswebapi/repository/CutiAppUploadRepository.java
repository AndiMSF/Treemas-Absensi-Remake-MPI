
package com.treemaswebapi.treemaswebapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.treemaswebapi.treemaswebapi.entity.CutiEntity.CutiAppUploadEntity;

@Repository
public interface CutiAppUploadRepository extends JpaRepository<CutiAppUploadEntity, Long> {

    Long countByFlgKet(String string);

    List<CutiAppUploadEntity> findByIsApprovedIsNull();

    Long countByFlgKetAndIsApprovedIsNull(String string);

    List<CutiAppUploadEntity> findByFlgKetAndIsApprovedIsNull(String string);
}



