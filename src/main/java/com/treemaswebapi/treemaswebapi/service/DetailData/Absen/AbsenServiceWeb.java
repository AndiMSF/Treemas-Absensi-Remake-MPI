package com.treemaswebapi.treemaswebapi.service.DetailData.Absen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import com.treemaswebapi.treemaswebapi.config.JwtService;
import com.treemaswebapi.treemaswebapi.entity.AbsenEntity.AbsenEntity;
import com.treemaswebapi.treemaswebapi.entity.JabatanEntity.JabatanEntity;
import com.treemaswebapi.treemaswebapi.entity.SysUserEntity.SysUserEntity;
import com.treemaswebapi.treemaswebapi.repository.AbsenRepository;
import com.treemaswebapi.treemaswebapi.repository.JabatanRepository;
import com.treemaswebapi.treemaswebapi.repository.SysUserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AbsenServiceWeb {
    private final AbsenRepository absenRepository;
    private final SysUserRepository sysUserRepository;
    private final JwtService jwtService;
    
    public ResponseEntity<Map<String, Object>> absenGet() {
        try {
            List<AbsenEntity> absenList  = absenRepository.findAll();
            List<Map<String, Object>> responseData = new ArrayList<>();

            for (AbsenEntity absen : absenList) {
                Map<String, Object> absenData = new HashMap<>();
                absenData.put("nik", absen.getNik());
                absenData.put("namaKaryawan", absen.getNama());
                absenData.put("tanggal", absen.getTglAbsen());
                absenData.put("lokasiMasuk", absen.getLokasiMsk());
                absenData.put("jamMasuk", absen.getJamMsk());
                absenData.put("lokasiPulang", absen.getLokasiPlg());
                absenData.put("jamPulang", absen.getJamPlg());
                absenData.put("catatanTerlambat", absen.getNoteTelatMsk());
                absenData.put("totalJamKerja", absen.getTotalJamKerja());
                absenData.put("isCuti", absen.getIsCuti());
                absenData.put("isOther", absen.getIsOther());
                absenData.put("isSakit", absen.getIsSakit());
                absenData.put("isWfh", absen.getIsWfh());
                absenData.put("isLembur", absen.getIsLembur());
                absenData.put("projectId", absen.getProjectId());
                // Cari role dari setiap nik di table sys_user
                Optional<SysUserEntity> sysUserOptional = sysUserRepository.findByUserId(absen.getNik());
                if (sysUserOptional.isPresent()) {
                    SysUserEntity sysUser = sysUserOptional.get();
                    absenData.put("role", sysUser.getRole());
                }

                    responseData.add(absenData);
                }
        
            Map<String, Object> response = new HashMap<>();
            response.put("status", "Success");
            response.put("message", "Retrieved");
            response.put("data", responseData);
        
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "Failed");
            response.put("message", "Failed to retrieve absen");
            response.put("error", e.getMessage());
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    public ResponseEntity<Map<String, Object>> absenGetSendiri(@RequestHeader("Authorization") String jwtToken) {
        try {
            // Cari siapa yang akses api ini
            String token = jwtToken.substring(7);
            String userToken = jwtService.extractUsername(token);
            List<AbsenEntity> absenList  = absenRepository.findAllByNik(userToken);
            List<Map<String, Object>> responseData = new ArrayList<>();

            for (AbsenEntity absen : absenList) {
                Map<String, Object> absenData = new HashMap<>();
                absenData.put("nik", absen.getNik());
                absenData.put("namaKaryawan", absen.getNama());
                absenData.put("tanggal", absen.getTglAbsen());
                absenData.put("lokasiMasuk", absen.getLokasiMsk());
                absenData.put("jamMasuk", absen.getJamMsk());
                absenData.put("lokasiPulang", absen.getLokasiPlg());
                absenData.put("jamPulang", absen.getJamPlg());
                absenData.put("catatanTerlambat", absen.getNoteTelatMsk());
                absenData.put("totalJamKerja", absen.getTotalJamKerja());
                absenData.put("isCuti", absen.getIsCuti());
                absenData.put("isOther", absen.getIsOther());
                absenData.put("isSakit", absen.getIsSakit());
                absenData.put("isWfh", absen.getIsWfh());
                absenData.put("isLembur", absen.getIsLembur());
                absenData.put("projectId", absen.getProjectId());
                absenData.put("isAbsen", absen.getIsAbsen());
                // Cari role dari setiap nik di table sys_user
                Optional<SysUserEntity> sysUserOptional = sysUserRepository.findByUserId(absen.getNik());
                if (sysUserOptional.isPresent()) {
                    SysUserEntity sysUser = sysUserOptional.get();
                    absenData.put("role", sysUser.getRole());
                }

                    responseData.add(absenData);
                }
        
            Map<String, Object> response = new HashMap<>();
            response.put("status", "Success");
            response.put("message", "Retrieved");
            response.put("data", responseData);
        
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "Failed");
            response.put("message", "Failed to retrieve absen");
            response.put("error", e.getMessage());
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
