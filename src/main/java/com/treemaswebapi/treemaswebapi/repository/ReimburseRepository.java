package com.treemaswebapi.treemaswebapi.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.treemaswebapi.treemaswebapi.entity.ReimburseEntity.ReimburseEntity;

@Repository
public interface ReimburseRepository extends JpaRepository<ReimburseEntity, Long> {
     List<ReimburseEntity> findByNikAndNamaAndTanggalAndProjectIdAndTotalJamKerja(
            String nik, String nama, Date tanggal, String project, BigDecimal totalJam);
     List<ReimburseEntity> findAllByNik(String nik);
}