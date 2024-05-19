package com.treemaswebapi.treemaswebapi.controller.MasterData.Jabatan.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JabatanRequest {
    private String jabatanId;
    private String namaJabatan;
    private String usrUpd;

}
