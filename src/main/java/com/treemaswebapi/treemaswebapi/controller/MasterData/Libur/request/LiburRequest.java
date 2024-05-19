package com.treemaswebapi.treemaswebapi.controller.MasterData.Libur.request;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LiburRequest {
    private Date tglLibur;
    private String keterangan;
    private String usrCrt;
    private String usrUpd;
    private String isCutiBersama;
}
