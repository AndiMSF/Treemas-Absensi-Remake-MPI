package com.treemaswebapi.treemaswebapi.service.RekapService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import com.treemaswebapi.treemaswebapi.entity.AbsenEntity.AbsenTrackingData.AbsenTrackingData;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ReimburseResponse {
    Long id;
    String hari;
    LocalDate tanggal;
    String flgKet;
    String namaProject;
    String lokasi;
    LocalTime jamMsk;
    LocalTime jamPlg;
    BigDecimal transport;
    Long uangMakan;
    String status;
    BigDecimal totalJamKerja;
    BigDecimal overtime;
}
