package com.treemaswebapi.treemaswebapi.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.treemaswebapi.treemaswebapi.entity.AbsenEntity.AbsenEntity;
import com.treemaswebapi.treemaswebapi.entity.ProjectEntity.ProjectEntity;

@Repository
public interface AbsenRepository extends JpaRepository<AbsenEntity, Long> {

    @Query("SELECT a FROM AbsenEntity a WHERE a.id = :id")
    AbsenEntity findByIdAbsen(Long id);

    List<AbsenEntity> findAllByProjectId(ProjectEntity projectId);

    List<AbsenEntity> findByNikAndTglAbsen(String nik, LocalDate currentDate);

    List<AbsenEntity> findAllByProjectIdAndTglAbsen(ProjectEntity projectId, LocalDate targetDate);

    List<AbsenEntity> findIdAbsenByNikAndIsAbsenIsNull(String nik);

    

    int countByIsAbsenAndNik(String isAbsen, String nik);

    int countByIsSakitAndNik(String isSakit, String nik);

    int countByNoteTelatMskIsNotNullAndNik(String nik);

    int countByNotePlgCepatIsNotNullAndNik(String nik);

    int countByIsCutiAndNik(String isCuti, String nik);

    int countByJamMskIsNullAndJamPlgIsNullAndNik(String nik);

    int countByJamMskIsNotNullAndNik(String nik);

    List<AbsenEntity> findIdAbsenByIsCuti(String string);

    List<AbsenEntity> findIdAbsenByIsCutiAndTglAbsen(String string, LocalDate date);

    List<AbsenEntity> findByTglAbsen(@Param("tglAbsen")LocalDate currentDate);

    AbsenEntity findByTglAbsenAndNik(LocalDate tanggalIni, String nik);

    boolean existsByNikAndTglAbsen(String nik, LocalDate now);

    List<AbsenEntity> findAllByNik(String nik);

    Optional<AbsenEntity> findByNik(String nik);

    List<AbsenEntity> findIdAbsenByNikAndIsAbsenIsNullAndJamMskIsNotNull(String nik);

    List<AbsenEntity> findByIsCuti(String string);
    // Retrieve AbsenEntity for a specific NIK for the current date
    List<AbsenEntity> findByNikInAndTglAbsen(List<String> nikList, LocalDate currentDate);

}
