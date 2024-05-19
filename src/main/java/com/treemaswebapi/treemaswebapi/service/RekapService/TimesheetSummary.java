package com.treemaswebapi.treemaswebapi.service.RekapService;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
@Builder
public class TimesheetSummary {
    private final BigDecimal totalJamReguler;
    private final BigDecimal totalJamLembur;
    private final BigDecimal totalJamKerja;
}
