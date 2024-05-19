package com.treemaswebapi.treemaswebapi.entity.AbsenEntity.AbsenTrackingData;

import java.time.LocalTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AbsenTrackingData {
    private String nama;
    private String nik;
    private String namaProject;
    private String catatanTelat;
    private LocalTime jamPlg;
    private String catatanPlgCpt;
    private String notePekerjaan;
    private List<Double> gpsLatitudeMsk;
    private Double gpsLatitudePlg;
    private List<Double> gpsLongitudeMsk;
    private Double gpsLongitudePlg;
    private LocalTime jamMsk;
}
// nik, nama, project, catatanTelat, jamPlg, lokasiPlg, catatanPlgCepat, timeSheet