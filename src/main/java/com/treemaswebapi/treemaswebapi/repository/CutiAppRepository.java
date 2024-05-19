package com.treemaswebapi.treemaswebapi.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.treemaswebapi.treemaswebapi.entity.CutiEntity.CutiAppEntity;

@Repository
public interface CutiAppRepository extends JpaRepository<CutiAppEntity, Long> {
    List<CutiAppEntity> findByFlgKet(String flgKet);

    Long countByFlgKet(String string);

    List<CutiAppEntity> findAllByNik(String nik);

    List<CutiAppEntity> findByIsApprovedIsNull();

    List<CutiAppEntity> findAllByNikAndFlgKet(String nik, String string);

    Long countByFlgKetAndIsApprovedIsNull(String string);

    List<CutiAppEntity> findByFlgKetAndIsApprovedIsNull(String string);

    int countByIsApprovedAndNikAndFlgKet(String string, String nik, String string2);

    List<CutiAppEntity> findByNikInAndTglMulai(List<String> memberNiks, LocalDate currentDate);

}
