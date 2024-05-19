
package com.treemaswebapi.treemaswebapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.treemaswebapi.treemaswebapi.entity.CutiEntity.CutiPenggantiEntity;

@Repository
public interface CutiPenggantiRepository extends JpaRepository<CutiPenggantiEntity, Long> {

    @Query("SELECT c.nik, SUM(c.jmlCuti) FROM CutiPenggantiEntity c GROUP BY c.nik")
    Long countJmlCutiPerNik(String nik);
}