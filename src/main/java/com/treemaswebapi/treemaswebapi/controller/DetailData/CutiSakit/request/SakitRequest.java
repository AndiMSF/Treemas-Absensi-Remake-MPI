package com.treemaswebapi.treemaswebapi.controller.DetailData.CutiSakit.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SakitRequest {
    
    private String image;
    private LocalDate tglMulai;
    private LocalDate tglSelesai;
    private LocalDate tglKembaliKerja;
    private BigDecimal jmlCuti;
    private String keperluanCuti;
}
