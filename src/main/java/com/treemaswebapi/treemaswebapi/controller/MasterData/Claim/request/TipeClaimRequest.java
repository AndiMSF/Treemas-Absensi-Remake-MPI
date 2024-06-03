package com.treemaswebapi.treemaswebapi.controller.MasterData.Claim.request;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TipeClaimRequest {
    private String namaClaim;
    private BigDecimal valueClaim;
    private String keterangan;
    private String usrCrt;
    private String usrUpd;
}
