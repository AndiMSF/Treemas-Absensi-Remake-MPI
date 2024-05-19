package com.treemaswebapi.treemaswebapi.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.treemaswebapi.treemaswebapi.entity.TimesheetEntity.TimesheetEntity;

@Repository
public interface TimesheetRepository extends JpaRepository<TimesheetEntity, Long> {

    List<TimesheetEntity> findAllByNik(String nik);
    Optional<TimesheetEntity> findByNik(String nik);
    TimesheetEntity findByNikAndTglMsk(String nik, LocalDate currentDate);
}