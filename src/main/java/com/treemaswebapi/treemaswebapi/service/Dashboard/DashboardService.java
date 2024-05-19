package com.treemaswebapi.treemaswebapi.service.Dashboard;

import java.time.LocalDate;
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
import com.treemaswebapi.treemaswebapi.controller.Dashboard.DashboardResponse;
import com.treemaswebapi.treemaswebapi.entity.AbsenEntity.AbsenEntity;
import com.treemaswebapi.treemaswebapi.entity.CutiEntity.CutiAppEntity;
import com.treemaswebapi.treemaswebapi.entity.KaryawanEntity.KaryawanEntity;
import com.treemaswebapi.treemaswebapi.entity.KaryawanEntity.KaryawanImageEntity;
import com.treemaswebapi.treemaswebapi.entity.SysUserEntity.SysUserEntity;
import com.treemaswebapi.treemaswebapi.repository.AbsenRepository;
import com.treemaswebapi.treemaswebapi.repository.CutiAppRepository;
import com.treemaswebapi.treemaswebapi.repository.KaryawanImageRepository;
import com.treemaswebapi.treemaswebapi.repository.KaryawanRepository;
import com.treemaswebapi.treemaswebapi.repository.SysUserMemberRepository;
import com.treemaswebapi.treemaswebapi.repository.SysUserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final AbsenRepository absenRepository;
    private final JwtService jwtService;
    private final KaryawanRepository karyawanRepository;
    private final KaryawanImageRepository KaryawanImageRepository;
    private final SysUserRepository sysUserRepository;
    private final SysUserMemberRepository sysUserMemberRepository;
    private final CutiAppRepository cutiAppRepository;

    public ResponseEntity<Map<String, Object>> dashboardGet(@RequestHeader("Authorization") String jwtToken, DashboardResponse dashboardResponse) {
        try {
            // Cari siapa yang akses api ini
            String token = jwtToken.substring(7);
            String nik = jwtService.extractUsername(token);

            Optional<KaryawanEntity> karyawan = karyawanRepository.findByNik(nik);
            Optional<SysUserEntity> sysUser = sysUserRepository.findByUserId(nik);
            String nama = karyawan.get().getNama();
            String isLocked = sysUser.get().getIsLocked();
            int masuk = absenRepository.countByJamMskIsNotNullAndNik(nik);// perubahan ini gue lakiun tanggal 13.12.2023 -Aliy
            int totalMasuk = masuk;
            int totalSakit = cutiAppRepository.countByIsApprovedAndNikAndFlgKet("1", nik, "sakit");
            int totalTelatMasuk = absenRepository.countByNoteTelatMskIsNotNullAndNik(nik);
            int totalPulangCepat = absenRepository.countByNotePlgCepatIsNotNullAndNik(nik);
            int totalCuti = cutiAppRepository.countByIsApprovedAndNikAndFlgKet("1", nik, "cuti");
            int totalTidakMasuk = absenRepository.countByJamMskIsNullAndJamPlgIsNullAndNik(nik);
            dashboardResponse.setNama(nama);
            dashboardResponse.setIsLocked(isLocked);
            dashboardResponse.setTotalMasuk(totalMasuk);
            dashboardResponse.setTotalSakit(totalSakit);
            dashboardResponse.setTotalTelatMasuk(totalTelatMasuk);
            dashboardResponse.setTotalCuti(totalCuti);
            dashboardResponse.setTotalTidakMasuk(totalTidakMasuk);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "Success");
            response.put("message", "Retrieved");
            response.put("data", dashboardResponse);
            
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

    public ResponseEntity<Map<String, Object>> dashboardGetFoto(@RequestHeader("Authorization") String jwtToken) {
        try {
            // Cari siapa yang akses api ini
            String token = jwtToken.substring(7);
            String nik = jwtService.extractUsername(token);

            Optional<KaryawanImageEntity> karyawan = KaryawanImageRepository.findByNik(nik);
            String karyawanImg = karyawan.get().getFoto();
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "Success");
            response.put("message", "Retrieved");
            response.put("data", karyawanImg);
            
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "Failed");
            response.put("message", "Failed to retrieve absen");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    public ResponseEntity<Map<String, Object>> dashboardGetMemberTahunan(
        DashboardResponse dashboardResponse,
        @RequestHeader("Authorization") String jwtToken
        ) {
        try {
            // Cari siapa yang akses api ini
            String token = jwtToken.substring(7);
            String nikLeader = jwtService.extractUsername(token);

            // Ambil semua member dari tabel absen
            List<String> memberNiks = sysUserMemberRepository.findAllByNikLeader(nikLeader); // Gantilah dengan metode yang sesuai
            System.out.println("INI MEMBER DARI NIK "+nikLeader+ " MEMBERNYA : "+memberNiks);
            // Buat daftar untuk menyimpan hasil perhitungan
            List<DashboardResponse> memberResponses = new ArrayList<>();

            for (String nik : memberNiks) {
            // Calculate totals for each member
            int masuk = absenRepository.countByJamMskIsNotNullAndNik(nik);
            int totalMasuk = masuk;
            int totalSakit = cutiAppRepository.countByIsApprovedAndNikAndFlgKet("1", nik, "sakit");
            int totalTelatMasuk = absenRepository.countByNoteTelatMskIsNotNullAndNik(nik);
            int totalPulangCepat = absenRepository.countByNotePlgCepatIsNotNullAndNik(nik);
            int totalCuti = cutiAppRepository.countByIsApprovedAndNikAndFlgKet("1", nik, "cuti");
            int totalTidakMasuk = absenRepository.countByJamMskIsNullAndJamPlgIsNullAndNik(nik);
            
            // Get Nama From SysUser table not SysUserMember
            String nama = sysUserRepository.findNameByNik(nik);


            // Create a DashboardResponse for each member
            DashboardResponse memberResponse = new DashboardResponse();
            memberResponse.setNik(nik);
            memberResponse.setNama(nama);
            memberResponse.setTotalMasuk(totalMasuk);
            memberResponse.setTotalSakit(totalSakit);
            memberResponse.setTotalTelatMasuk(totalTelatMasuk);
            memberResponse.setTotalCuti(totalCuti);
            memberResponse.setTotalTidakMasuk(totalTidakMasuk);

            memberResponses.add(memberResponse);
        }

            Map<String, Object> response = new HashMap<>();
            response.put("status", "Success");
            response.put("message", "Retrieved");
            response.put("data", memberResponses);
            
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

    public ResponseEntity<Map<String, Object>> dashboardGetMemberHarian(
        DashboardResponse dashboardResponse,
        @RequestHeader("Authorization") String jwtToken
) {
    try {
        // Cari siapa yang akses api ini
        String token = jwtToken.substring(7);
        String nikLeader = jwtService.extractUsername(token);

        // Ambil semua member dari tabel absen
        List<String> memberNiks = sysUserMemberRepository.findAllByNikLeader(nikLeader);
        System.out.println("INI MEMBER DARI NIK " + nikLeader + " MEMBERNYA : " + memberNiks);

        // Calculate totals for each type for all members for the current date
        LocalDate currentDate = LocalDate.now();
        List<AbsenEntity> absenEntities = absenRepository.findByNikInAndTglAbsen(memberNiks, currentDate);
        List<CutiAppEntity> cutiEntities = cutiAppRepository.findByNikInAndTglMulai(memberNiks, currentDate);
        int totalMasuk = 0;
        int totalSakit = 0;
        int totalTelatMasuk = 0;
        int totalCuti = 0;
        int totalTidakMasuk = 0;

        for (AbsenEntity absenEntity : absenEntities) {
            if ("1".equals(absenEntity.getIsAbsen())) {
                totalMasuk++;
            }

            if ("1".equals(absenEntity.getIsSakit())) {
                totalSakit++;
            }

            if (absenEntity.getNoteTelatMsk() != null) {
                totalTelatMasuk++;
            }

            if ("1".equals(absenEntity.getIsCuti())) {
                totalCuti++;
            }

            if (absenEntity.getJamMsk() == null && absenEntity.getJamPlg() == null) {
                totalTidakMasuk++;
            }
        }

        for (CutiAppEntity cutiAppEntity : cutiEntities) {

            if ("1".equals(cutiAppEntity.getIsApproved())) {
                if ("sakit".equals(cutiAppEntity.getFlgKet())){
                    totalSakit++;
                }
            }

            if ("1".equals(cutiAppEntity.getIsApproved())) {
                if ("cuti".equals(cutiAppEntity.getFlgKet())){
                    totalCuti++;
                }
            }
        }

        // Create a DashboardResponse for the totals
        DashboardResponse totalResponse = new DashboardResponse();
        totalResponse.setTotalMasuk(totalMasuk);
        totalResponse.setTotalSakit(totalSakit);
        totalResponse.setTotalTelatMasuk(totalTelatMasuk);
        totalResponse.setTotalCuti(totalCuti);
        totalResponse.setTotalTidakMasuk(totalTidakMasuk);

        // Add the totals to the response
        Map<String, Object> response = new HashMap<>();
        response.put("status", "Success");
        response.put("message", "Retrieved");
        response.put("data", totalResponse);

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
