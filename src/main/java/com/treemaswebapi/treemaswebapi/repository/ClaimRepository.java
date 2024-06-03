package com.treemaswebapi.treemaswebapi.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.treemaswebapi.treemaswebapi.entity.ClaimEntity.ClaimEntity;

@Repository
public interface ClaimRepository extends JpaRepository<ClaimEntity, Long> {

    List<ClaimEntity> findAllByNik(String nik);

    List<ClaimEntity> findByNikAndTanggalBetween(String nik, LocalDate startDate, LocalDate endDate);
}
