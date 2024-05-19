package com.treemaswebapi.treemaswebapi.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.treemaswebapi.treemaswebapi.entity.CutiEntity.CutiEntity;

@Repository
public interface CutiRepository extends JpaRepository<CutiEntity, Long> {
    List<CutiEntity> findByFlagApp(String flagApp);

    List<CutiEntity> findAllByNik(String nik);

    List<CutiEntity> findAllByNikAndFlgKet(String nik, String string);

    List<CutiEntity> findAllByFlgKet(String string);

    List<CutiEntity> findByIsApprovedAndTglMulai(String string, LocalDate date);

    Optional<CutiEntity> findByNik(String nikKaryawan);
}