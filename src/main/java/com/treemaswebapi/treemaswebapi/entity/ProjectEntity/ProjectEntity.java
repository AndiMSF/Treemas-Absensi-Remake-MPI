package com.treemaswebapi.treemaswebapi.entity.ProjectEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_project", schema = "public")
public class ProjectEntity {

    @Id
    @Column(name = "project_id")
    private String projectId;

    @Column(name = "nama_project")
    private String namaProject;

    @Column(name = "lokasi")
    private String lokasi;

    @Column(name = "jam_kerja")
    private BigDecimal jamKerja;

    @Column(name = "jam_masuk")
    private LocalTime jamMasuk;

    @Column(name = "jam_keluar")
    private LocalTime jamKeluar;

    @Column(name = "no_tlpn")
    private String noTlpn;

    @Column(name = "biaya_reimburse")
    private BigDecimal biayaReimburse;

    @Column(name = "gps_latitude")
    private Double gpsLatitude;

    @Column(name = "gps_longitude")
    private Double gpsLongitude;

    @Column(name = "kota")
    private String kota;

    @Column(name = "jrk_max")
    private String jrkMax;

    @Column(name = "usrupd")
    private String usrupd;

    @Column(name = "dtmupd")
    private Timestamp dtmupd;

    public ProjectEntity(String projectId) {
        this.projectId = projectId;
    }
}
