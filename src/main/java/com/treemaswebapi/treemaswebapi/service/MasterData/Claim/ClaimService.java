package com.treemaswebapi.treemaswebapi.service.MasterData.Claim;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import com.treemaswebapi.treemaswebapi.config.JwtService;
import com.treemaswebapi.treemaswebapi.controller.MasterData.Claim.request.TipeClaimRequest;
import com.treemaswebapi.treemaswebapi.entity.ClaimEntity.ClaimEntity;
import com.treemaswebapi.treemaswebapi.entity.ClaimEntity.TipeClaimEntity;
import com.treemaswebapi.treemaswebapi.entity.KaryawanEntity.KaryawanEntity;
import com.treemaswebapi.treemaswebapi.repository.ClaimRepository;
import com.treemaswebapi.treemaswebapi.repository.KaryawanRepository;
import com.treemaswebapi.treemaswebapi.repository.TipeClaimRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClaimService {
    
    private final TipeClaimRepository tipeClaimRepository;
    private final KaryawanRepository karyawanRepository;
    private final JwtService jwtService;
    private final ClaimRepository claimRepository;
    
    public ResponseEntity<Map<String, Object>> tipeClaimAdd(
        @RequestHeader("Authorization") String jwtToken,
        TipeClaimRequest request
    ) {
        try {
              // Cari siapa yang akses api ini
            String token = jwtToken.substring(7);
            String userToken = jwtService.extractUsername(token);
            
            Optional<KaryawanEntity> user = karyawanRepository.findByNik(userToken);
            String nama = user.get().getNama();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDtmCrt = LocalDateTime.now().format(formatter);

            System.out.println(nama);
            var tipeClaim = TipeClaimEntity.builder()
                .namaClaim(request.getNamaClaim())
                .valueClaim(request.getValueClaim())
                .keterangan(request.getKeterangan())
                .usrCrt(nama)
                .dtmCrt(formattedDtmCrt)
                .build();
            tipeClaimRepository.save(tipeClaim);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "Success");
            response.put("message", "Tipe Claim Created");
            response.put("data", tipeClaim);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "Failed");
            response.put("message", "Failed To Create Tipe Claim");
            response.put("error", e.getMessage());
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    public ResponseEntity<Map<String, Object>> tipeClaimUpdate(
        @RequestHeader("Authorization") String jwtToken,
        TipeClaimRequest request,
        Long id
    ) {
        try {
            // Cari siapa yang akses api ini
            String token = jwtToken.substring(7);
            String userToken = jwtService.extractUsername(token);
            
            Optional<KaryawanEntity> user = karyawanRepository.findByNik(userToken);
            String nama = user.get().getNama();
            
            // Cari Tipe Claim berdasarkan ID
            Optional<TipeClaimEntity> tipeClaimOptional = tipeClaimRepository.findById(id);
            if (tipeClaimOptional.isPresent()) {
                TipeClaimEntity tipeClaim = tipeClaimOptional.get();
                tipeClaim.setNamaClaim(request.getNamaClaim());
                tipeClaim.setValueClaim(request.getValueClaim());
                tipeClaim.setKeterangan(request.getKeterangan());
                tipeClaim.setUsrUpd(nama);
                // Set dtmUpd manually with the current timestamp as a formatted string
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                tipeClaim.setDtmUpd(LocalDateTime.now().format(formatter));
                tipeClaimRepository.save(tipeClaim);
            
            

            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "Success");
            response.put("message", "Tipe claim created");
            response.put("data", tipeClaim);

            return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "Failed");
                response.put("message", "Tipe claim not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "Failed");
            response.put("message", "Failed To Create Tipe Claim");
            response.put("error", e.getMessage());
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    public ResponseEntity<Map<String, String>> tipeClaimDelete(
        Long id
    ) {
        try {
            // Cari Tipe Claim berdasarkan ID
            Optional<TipeClaimEntity> tipeClaimOptional = tipeClaimRepository.findById(id);
            if (tipeClaimOptional.isPresent()) {
                tipeClaimRepository.deleteById(id);

                Map<String, String> response = new HashMap<>();
                response.put("status", "Success");
                response.put("message", "Tipe claim deleted");
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                Map<String, String> response = new HashMap<>();
                response.put("status", "Failed");
                response.put("message", "Tipe claim not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "Failed");
            response.put("message", "Failed To Delete Tipe Claim");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    public ResponseEntity<Map<String, Object>> tipeClaimGet() {
        try {
            List<TipeClaimEntity> tipeClaims = tipeClaimRepository.findAll();

            Map<String, Object> response = new HashMap<>();
            response.put("status", "Success");
            response.put("message", "Retrieved");
            response.put("data", tipeClaims);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "Failed");
            response.put("message", "Failed to retrieve tipe claims");
            response.put("error", e.getMessage());
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
