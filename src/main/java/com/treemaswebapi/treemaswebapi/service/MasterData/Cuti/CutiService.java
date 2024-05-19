package com.treemaswebapi.treemaswebapi.service.MasterData.Cuti;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import com.treemaswebapi.treemaswebapi.config.JwtService;
import com.treemaswebapi.treemaswebapi.controller.MasterData.Cuti.request.MasterCutiRequest;
import com.treemaswebapi.treemaswebapi.entity.CutiEntity.MasterCutiEntity;
import com.treemaswebapi.treemaswebapi.entity.KaryawanEntity.KaryawanEntity;
import com.treemaswebapi.treemaswebapi.repository.KaryawanRepository;
import com.treemaswebapi.treemaswebapi.repository.MasterCutiRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CutiService {
    
    private final JwtService jwtService;
    private final MasterCutiRepository masterCutiRepository;
    private final KaryawanRepository karyawanRepository;
    
    public ResponseEntity<Map<String, Object>> cutiAdd(
        MasterCutiRequest request,
        @RequestHeader("Authorization") String jwtToken
    ) {
        try {
            // Cari siapa yang akses api ini
            String token = jwtToken.substring(7);
            String userToken = jwtService.extractUsername(token);
            
            Optional<KaryawanEntity> user = karyawanRepository.findByNik(userToken);
            String nama = user.get().getNama();

            long currentTimeMillis = System.currentTimeMillis();
            Timestamp dtmCrt = new Timestamp(currentTimeMillis - (currentTimeMillis % 1000));

            System.out.println(nama);
            var masterCutiEntity = MasterCutiEntity.builder()
                .id(request.getId())
                .cutiDesc(request.getCutiDesc())
                .value(request.getValue())
                .dtmCrt(dtmCrt)
                .usrCrt(nama)
            .build();
            
            masterCutiRepository.save(masterCutiEntity);
            

            Map<String, Object> response = new HashMap<>();
            response.put("status", "Success");
            response.put("message", "Cuti Created");

            Map<String, Object> data = new HashMap<>();
            data.put("id", masterCutiEntity.getId());
            data.put("cutiDesc", masterCutiEntity.getCutiDesc());
            data.put("value", masterCutiEntity.getValue());
            data.put("usrCrt", masterCutiEntity.getUsrCrt());
            data.put("dtmCrt", masterCutiEntity.getDtmCrt());  // Include dtmCrt in the data

            response.put("data", data);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "Failed");
            response.put("message", "Failed To Create Cuti");
            response.put("error", e.getMessage());
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    public ResponseEntity<Map<String, Object>> cutiGet() {
        try {
            List<MasterCutiEntity> masterCuti = masterCutiRepository.findAll();

            Map<String, Object> response = new HashMap<>();
            response.put("status", "Success");
            response.put("message", "Retrieved");
            response.put("data", masterCuti);
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

    public ResponseEntity<Map<String, Object>> cutiUpdate(
        @RequestHeader("Authorization") String jwtToken,
        MasterCutiRequest request,
        String id
    ) {
        try {
            // Cari siapa yang akses api ini
            String token = jwtToken.substring(7);
            String userToken = jwtService.extractUsername(token);
            
            Optional<KaryawanEntity> user = karyawanRepository.findByNik(userToken);
            String nama = user.get().getNama();

            // Cari Tipe Claim berdasarkan ID
            Optional<MasterCutiEntity> masterCutiOptional = masterCutiRepository.findById(id);
            if (masterCutiOptional.isPresent()) {
                MasterCutiEntity masterCutiEntity = masterCutiOptional.get();
                String foundUsrCrt = masterCutiEntity.getUsrCrt();
                Timestamp foundDtmCrt = masterCutiEntity.getDtmCrt();

                long currentTimeMillis = System.currentTimeMillis();
                Timestamp dtmUpd = new Timestamp(currentTimeMillis - (currentTimeMillis % 1000));
                // Delete the existing entity
                masterCutiRepository.deleteById(id);
                // Create a new entity with the updated ID
                MasterCutiEntity newMasterCutiEntity = new MasterCutiEntity();
                newMasterCutiEntity.setId(request.getId());
                newMasterCutiEntity.setValue(request.getValue());
                newMasterCutiEntity.setCutiDesc(request.getCutiDesc());
                newMasterCutiEntity.setUsrUpd(nama);
                newMasterCutiEntity.setDtmUpd(dtmUpd);
                newMasterCutiEntity.setUsrCrt(foundUsrCrt);
                newMasterCutiEntity.setDtmCrt(foundDtmCrt);
                // Save the new entity
                masterCutiRepository.save(newMasterCutiEntity);

                Map<String, Object> response = new HashMap<>();
                response.put("status", "Success");
                response.put("message", "Master Cuti Updated");
                response.put("data", newMasterCutiEntity);

            return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "Failed");
                response.put("message", "Master Cuti not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "Failed");
            response.put("message", "Failed To Update Master Cuti");
            response.put("error", e.getMessage());
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    public ResponseEntity<Map<String, String>> cutiDelete(
        String id
    ) {
        try {
            // Cari Announcement berdasarkan ID
            Optional<MasterCutiEntity> masterCutiOptional = masterCutiRepository.findById(id);
            if (masterCutiOptional.isPresent()) {
                masterCutiRepository.deleteById(id);

                Map<String, String> response = new HashMap<>();
                response.put("status", "Success");
                response.put("message", "Cuti deleted");
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                Map<String, String> response = new HashMap<>();
                response.put("status", "Failed");
                response.put("message", "Cuti not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "Failed");
            response.put("message", "Failed To Delete Cuti");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
