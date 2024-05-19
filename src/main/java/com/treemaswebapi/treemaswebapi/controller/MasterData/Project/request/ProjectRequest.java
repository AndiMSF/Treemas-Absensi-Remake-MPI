package com.treemaswebapi.treemaswebapi.controller.MasterData.Project.request;

import java.math.BigDecimal;
import java.sql.Time;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectRequest {
    private String projectId;
    private String namaProject;
    private String noTlpn;
    private String kota;
    private String lokasi;
    private Double gpsLatitude;
    private Double gpsLongitude;
    private BigDecimal biayaReimburse;
    private String jrkMax;
    private BigDecimal jamKerja;
    private LocalTime jamMasuk;
    private LocalTime jamKeluar;

}
