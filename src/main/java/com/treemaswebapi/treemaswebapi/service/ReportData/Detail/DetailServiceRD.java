package com.treemaswebapi.treemaswebapi.service.ReportData.Detail;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
// Import statements
import java.util.Date;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.treemaswebapi.treemaswebapi.entity.AbsenEntity.AbsenEntity;
import com.treemaswebapi.treemaswebapi.repository.AbsenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DetailServiceRD {

    private final AbsenRepository absenRepository;
    
    public ResponseEntity<Map<String, Object>> detailGetAll() {
        try {
        List<AbsenEntity> absens = absenRepository.findAll(); // Fetch all records from the AnnouncementEntity table

            List<Map<String, Object>> formattedClaims = new ArrayList<>();

            for (AbsenEntity absen : absens) {
                Map<String, Object> formattedClaim = new HashMap<>();
                formattedClaim.put("id", absen.getId());

                // Mendapatkan tanggal hari ini
                Date today = Calendar.getInstance().getTime();
                formattedClaim.put("tanggal", today);
                
                formattedClaim.put("nik", absen.getNik());
                formattedClaim.put("namaKaryawan", absen.getNama());
                formattedClaim.put("tanggal", today);
                formattedClaim.put("jamMasuk", absen.getJamMsk());
                formattedClaim.put("jamKeluar", absen.getJamPlg());
                // formattedClaim.put("totalJamKeluar", absen.getTotalJamKerja().getKeterangan());
                // formattedClaim.put("transport", absen.getKeterangan());
                // formattedClaim.put("uangMakan", absen.getKeterangan());
                // formattedClaim.put("overtime", absen.getKeterangan());
                // formattedClaim.put("catatan", absen());

                formattedClaims.add(formattedClaim);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("status", "Success");
            response.put("message", "Claim Retrieved");
            response.put("data", formattedClaims);

            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "Failed");
            response.put("message", "Failed to retrieve claim");
            response.put("error", e.getMessage());
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
