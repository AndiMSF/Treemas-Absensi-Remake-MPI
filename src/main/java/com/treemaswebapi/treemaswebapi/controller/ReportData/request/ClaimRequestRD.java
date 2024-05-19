package com.treemaswebapi.treemaswebapi.controller.ReportData.request;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClaimRequestRD {
    private String idClaim;
    private String keterangan;
    private BigDecimal nominal;
    private String image64;
    private Long selectedTipeClaim;
}
