
package com.treemaswebapi.treemaswebapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.treemaswebapi.treemaswebapi.entity.CutiEntity.CancelCutiEntity;

@Repository
public interface CancelCutiRepository extends JpaRepository<CancelCutiEntity, Long> {
}


