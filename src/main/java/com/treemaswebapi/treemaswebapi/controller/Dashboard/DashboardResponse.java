package com.treemaswebapi.treemaswebapi.controller.Dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class DashboardResponse {
    int totalCuti;
    int totalMasuk;
    int totalPulangCepat;
    int totalSakit;
    int totalTelatMasuk;
    int totalTidakMasuk;
    String nama;
    String isLocked;

    // Tambahan Web
    String nik;
}