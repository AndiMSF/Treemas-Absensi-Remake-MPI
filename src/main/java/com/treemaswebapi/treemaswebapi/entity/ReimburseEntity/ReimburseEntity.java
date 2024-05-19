package com.treemaswebapi.treemaswebapi.entity.ReimburseEntity; // Replace with your actual package name

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalTime;

// project_id FK dari tbl_project

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_reimburse", schema = "public")
public class ReimburseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nik")
    private String nik;

    @Column(name = "nama")
    private String nama;

    @Column(name = "hari")
    private String hari;

    @Column(name = "tanggal")
    private Date tanggal;

    @Column(name = "project_id")
    private String projectId;

    @Column(name = "jam_masuk")
    private LocalTime jamMasuk;

    @Column(name = "jam_keluar")
    private LocalTime jamKeluar;

    @Column(name = "total_jam_kerja")
    private BigDecimal totalJamKerja;

    @Column(name = "transport")
    private BigDecimal transport;

    @Column(name = "uang_makan")
    private BigDecimal uangMakan;

    @Column(name = "flg_ket")
    private String flgKet;

    @Column(name = "usrcrt")
    private String usrCrt;

    @Column(name = "dtmcrt")
    private Timestamp dtmCrt;

    @Column(name = "usrapp")
    private String usrApp;

    @Column(name = "dtmapp")
    private Timestamp dtmApp;

    @Column(name = "status")
    private String status;

    @Column(name = "note_app")
    private String noteApp;
}
