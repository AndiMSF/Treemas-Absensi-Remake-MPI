package com.treemaswebapi.treemaswebapi.controller.AbsenController.request;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OtherProjectReq {
    private String namaProject;
    private LocalTime jamMsk;
    private String lokasiProject;
    private String jarakMsk;
    private String keteranganOther;
}
