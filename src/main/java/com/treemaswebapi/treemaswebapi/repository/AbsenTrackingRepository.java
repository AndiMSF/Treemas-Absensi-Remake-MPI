package com.treemaswebapi.treemaswebapi.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.treemaswebapi.treemaswebapi.entity.AbsenEntity.AbsenEntity;
import com.treemaswebapi.treemaswebapi.entity.AbsenEntity.AbsenTrackingEntity;

@Repository
public interface AbsenTrackingRepository extends JpaRepository<AbsenTrackingEntity, Long> {

    List<AbsenTrackingEntity> findByTglAbsenAndNik(LocalDate currentDate, String nik);
}