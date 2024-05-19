package com.treemaswebapi.treemaswebapi.controller.AbsenController;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder

public class AbsenBelumPulangResponse {
    private String projectName;
    private String noteTelatMsk;
    private LocalTime jamMsk;
    private String lokasiProject;
    private LocalDate tglAbsen;
    private Long idAbsen;
    private Double longitudeProject;
    private Double latitudeProject;

}
