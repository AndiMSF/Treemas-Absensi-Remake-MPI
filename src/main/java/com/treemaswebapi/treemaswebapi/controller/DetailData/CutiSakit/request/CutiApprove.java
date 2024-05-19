package com.treemaswebapi.treemaswebapi.controller.DetailData.CutiSakit.request;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CutiApprove {
    private String noteApp;
    private String isApproved;
    private BigDecimal jumlahCuti;
}
