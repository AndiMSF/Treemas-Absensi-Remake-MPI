package com.treemaswebapi.treemaswebapi.service.RekapService;
import java.math.BigDecimal;
import java.time.LocalDate;

import com.treemaswebapi.treemaswebapi.entity.CutiEntity.MasterCutiEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CutiResponse {

    private Long id;
    private String nik;
    private String nama;
    private LocalDate tglMulai;
    private LocalDate tglSelesai;
    private LocalDate tglKembaliKerja;
    private String keperluanCuti;
    private String alamatCuti;
    private String isApproved;
    private BigDecimal jmlCutiBersama;
    private BigDecimal jmlCutiKhusus;
    private BigDecimal sisaCuti;
    private BigDecimal jmlCuti;
    private MasterCutiEntity jenisCuti;
    private String status;
}
