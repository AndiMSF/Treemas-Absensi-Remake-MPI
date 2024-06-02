package com.treemaswebapi.treemaswebapi.controller.AbsenController.request;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

import com.treemaswebapi.treemaswebapi.entity.ProjectEntity.ProjectEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AbsenRequest {

    private ProjectEntity projectId;
    private String nik;
    private String nama;
    private String hari;
    private LocalDate tglAbsen;
    private Double gpsLatitudeMsk;
    private Double gpsLongitudeMsk;
    private String lokasiMsk;
    private String jarakMsk;
    private LocalTime jamMsk;
    private Double gpsLatitudePlg;
    private Double gpsLongitudePlg;
    private String lokasiPlg;
    private String jarakPlg;
    private LocalTime jamPlg;
    private String notePekerjaan;
    private String noteTelatMsk;
    private String notePlgCepat;
    private String noteOther;
    private BigDecimal totalJamKerja;
    private String isLembur;
    private String isAbsen;
    private String isSakit;
    private String isCuti;
    private String isLibur;
    private String isOther;
    private String isWfh;
    private String usrCrt;
    private Timestamp dtmCrt;
    private String usrApp;
    private Timestamp dtmApp;
    private String noteApp;
    private String photoAbsen;
    private String keteranganLupaPulang;
    private String image;

    // Buat web langsung kirim projectId nya tanpa dari table ProjectId
    private String projectIdWeb;
}






