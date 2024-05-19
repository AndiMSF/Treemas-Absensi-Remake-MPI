package com.treemaswebapi.treemaswebapi.service.RekapService;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TotalReimburse {
    private BigDecimal reimburse;
    private BigDecimal dataVoice;
    private BigDecimal lainlain;
    private BigDecimal keseluruhan;
}
