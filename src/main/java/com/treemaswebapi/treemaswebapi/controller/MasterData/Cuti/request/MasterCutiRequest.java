package com.treemaswebapi.treemaswebapi.controller.MasterData.Cuti.request;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MasterCutiRequest {
    private String id;
    private String cutiDesc;
    private BigDecimal value;
    private String usrCrt;
    private String usrUpd;
}
