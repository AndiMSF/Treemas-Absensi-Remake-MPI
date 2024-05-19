package com.treemaswebapi.treemaswebapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.treemaswebapi.treemaswebapi.entity.KaryawanEntity.KaryawanEntity;

@Repository
public interface KaryawanRepository extends JpaRepository<KaryawanEntity, String> {
    Optional<KaryawanEntity> findByNik(String nik);
    Optional<KaryawanEntity> findByNama(String nama);    
    @Query("SELECT k.nama FROM KaryawanEntity k WHERE k.nik = :nik")
    String findNamaByNik(@Param("nik") String nik);

}
