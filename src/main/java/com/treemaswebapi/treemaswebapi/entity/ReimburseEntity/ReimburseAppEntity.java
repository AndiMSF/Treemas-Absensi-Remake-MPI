package com.treemaswebapi.treemaswebapi.entity.ReimburseEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

import com.treemaswebapi.treemaswebapi.entity.ProjectEntity.ProjectEntity;

// project_id FK dari tbl_project

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_reimburse_app", schema = "public")
public class ReimburseAppEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private ProjectEntity projectId;

    @Column(name = "nik")
    private String nik;

    @Column(name = "nama")
    private String nama;

    @Column(name = "hari")
    private String hari;

    @Column(name = "tgl_absen")
    private LocalDate tglAbsen;

    @Column(name = "gps_latitude_msk")
    private Double gpsLatitudeMsk;

    @Column(name = "gps_longitude_msk")
    private Double gpsLongitudeMsk;

    @Column(name = "lokasi_msk")
    private String lokasiMsk;

    @Column(name = "jarak_msk")
    private String jarakMsk;

    @Column(name = "jam_msk")
    private LocalTime jamMsk;

    @Column(name = "gps_latitude_plg")
    private Double gpsLatitudePlg;

    @Column(name = "gps_longitude_plg")
    private Double gpsLongitudePlg;

    @Column(name = "lokasi_plg")
    private String lokasiPlg;

    @Column(name = "jarak_plg")
    private String jarakPlg;

    @Column(name = "jam_plg")
    private LocalTime jamPlg;

    @Column(name = "note_pekerjaan")
    private String notePekerjaan;

    @Column(name = "note_telat_msk")
    private String noteTelatMsk;

    @Column(name = "note_plg_cepat")
    private String notePlgCepat;

    @Column(name = "note_other")
    private String noteOther;

    @Column(name = "total_jam_kerja")
    private BigDecimal totalJamKerja;

    @Column(name = "is_lembur")
    private String isLembur;

    @Column(name = "is_absen")
    private String isAbsen;

    @Column(name = "is_sakit")
    private String isSakit;

    @Column(name = "is_cuti")
    private String isCuti;

    @Column(name = "is_libur")
    private String isLibur;

    @Column(name = "is_other")
    private String isOther;

    @Column(name = "is_wfh")
    private String isWfh;

    @Column(name = "is_approve")
    private String isApprove;

    @Column(name = "flag_app")
    private String flagApp;

    @Column(name = "keterangan")
    private String keterangan;

    @Column(name = "usrupd")
    private String usrUpd;

    @Column(name = "dtmupd")
    private Timestamp dtmUpd;
}
