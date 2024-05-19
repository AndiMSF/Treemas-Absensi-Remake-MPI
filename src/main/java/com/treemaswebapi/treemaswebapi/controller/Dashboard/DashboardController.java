package com.treemaswebapi.treemaswebapi.controller.Dashboard;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.treemaswebapi.treemaswebapi.service.Dashboard.DashboardService;
import com.treemaswebapi.treemaswebapi.service.MasterData.Karyawan.KaryawanService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    
    private final DashboardService service;
    private final KaryawanService karyawanService;

    @GetMapping("/data-kehadiran")
    public ResponseEntity<Map<String, Object>> dashboardGet(
        @RequestHeader("Authorization") String jwtToken,
        DashboardResponse dashboardResponse
        ) {
        ResponseEntity<Map<String, Object>> response = service.dashboardGet(jwtToken, dashboardResponse);
        return response;
    }

    @GetMapping("/navbar")
    public ResponseEntity<Map<String, Object>> dashboardGetFoto(
        @RequestHeader("Authorization") String jwtToken
        ) {
        ResponseEntity<Map<String, Object>> response = service.dashboardGetFoto(jwtToken);
        return response;
    }


    @GetMapping("/main")
    public ResponseEntity<Map<String, Object>> getDataPribadi(@RequestHeader("Authorization") String token, DashboardResponse dashboardResponse)
    {
        return karyawanService.getDataPribadi(token, dashboardResponse);
    }

    @GetMapping("/data-kehadiran-member-hari")
        public ResponseEntity<Map<String, Object>> dashboardGetMemberHarian(
            DashboardResponse dashboardResponse,
            @RequestHeader("Authorization") String jwtToken
            ) {
            ResponseEntity<Map<String, Object>> response = service.dashboardGetMemberHarian(dashboardResponse, jwtToken);
            return response;
        }

    @GetMapping("/data-kehadiran-member")
    public ResponseEntity<Map<String, Object>> dashboardGetMemberTahunan(
        DashboardResponse dashboardResponse,
        @RequestHeader("Authorization") String jwtToken
        ) {
        ResponseEntity<Map<String, Object>> response = service.dashboardGetMemberTahunan(dashboardResponse, jwtToken);
        return response;
    }
}
