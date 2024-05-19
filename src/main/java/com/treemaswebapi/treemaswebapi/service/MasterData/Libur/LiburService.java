package com.treemaswebapi.treemaswebapi.service.MasterData.Libur;

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
import com.treemaswebapi.treemaswebapi.controller.MasterData.Libur.request.LiburRequest;
import com.treemaswebapi.treemaswebapi.entity.KaryawanEntity.KaryawanEntity;
import com.treemaswebapi.treemaswebapi.entity.LiburEntity.LiburEntity;
import com.treemaswebapi.treemaswebapi.repository.KaryawanRepository;
import com.treemaswebapi.treemaswebapi.repository.LiburRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LiburService {

    private final JwtService jwtService;
    private final KaryawanRepository karyawanRepository;
    private final LiburRepository liburRepository;
    
    public ResponseEntity<Map<String, Object>> liburAdd(
        LiburRequest request,
        @RequestHeader("Authorization") String jwtToken
    ){
        try {
            // Cari siapa yang akses api ini
            String token = jwtToken.substring(7);
            String userToken = jwtService.extractUsername(token);
            
            Optional<KaryawanEntity> user = karyawanRepository.findByNik(userToken);
            String nama = user.get().getNama();
            long currentTimeMillis = System.currentTimeMillis();
            Timestamp dtmCrt = new Timestamp(currentTimeMillis - (currentTimeMillis % 1000));

            var liburEntity = LiburEntity.builder()
                .tglLibur(request.getTglLibur())
                .keterangan(request.getKeterangan())
                .dtmCrt(dtmCrt)
                .usrCrt(nama)
                .isCutiBersama(request.getIsCutiBersama())
            .build();
            liburRepository.save(liburEntity);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "Success");
            response.put("message", "Libur Created");

            Map<String, Object> data = new HashMap<>();
            data.put("tanggal", liburEntity.getTglLibur());
            data.put("keterangan", liburEntity.getKeterangan());
            data.put("usrcrt", liburEntity.getUsrCrt());
            data.put("dtmcrt", liburEntity.getDtmCrt());

            response.put("data", data);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "Failed");
            response.put("message", "Failed to Create Libur!");
            response.put("error", e.getMessage());
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    public ResponseEntity<Map<String, Object>> liburGet() {
        try {
            List<LiburEntity> libur = liburRepository.findAll();

            Map<String, Object> response = new HashMap<>();
            response.put("status", "Success");
            response.put("message", "Retrieved");
            response.put("data", libur);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "Failed");
            response.put("message", "Failed to retrieve tipe Libur");
            response.put("error", e.getMessage());
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    public ResponseEntity<Map<String, Object>> liburUpdate(
        LiburRequest request,
        Long id,
        @RequestHeader("Authorization") String jwtToken
    ) {
        try {

            // Cari siapa yang akses api ini
            String token = jwtToken.substring(7);
            String userToken = jwtService.extractUsername(token);
            
            Optional<KaryawanEntity> user = karyawanRepository.findByNik(userToken);
            String nama = user.get().getNama();

            Optional<LiburEntity> liburOptional = liburRepository.findById(id);
            long currentTimeMillis = System.currentTimeMillis();
            Timestamp dtmUpd = new Timestamp(currentTimeMillis - (currentTimeMillis % 1000));

            if (liburOptional.isPresent()) {
                LiburEntity newLiburEntity = liburOptional.get();

                newLiburEntity.setTglLibur(request.getTglLibur());
                newLiburEntity.setKeterangan(request.getKeterangan());
                newLiburEntity.setDtmUpd(dtmUpd);
                newLiburEntity.setUsrUpd(nama);
                newLiburEntity.setIsCutiBersama(request.getIsCutiBersama());

                liburRepository.save(newLiburEntity);
                
                Map<String, Object> response = new HashMap<>();
                response.put("status", "Success");
                response.put("message", "Libur Updated");
                response.put("data", newLiburEntity);

                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "Failed");
                response.put("message", "Libur Not Found");

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "Failed");
            response.put("message", "Failed To Create Libur");
            response.put("error", e.getMessage());
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    public ResponseEntity<Map<String, String>> liburDelete(
        Long id
    ) {
        try {
            // Cari Announcement berdasarkan ID
            Optional<LiburEntity> liburOptional = liburRepository.findById(id);
            if (liburOptional.isPresent()) {
                liburRepository.deleteById(id);

                Map<String, String> response = new HashMap<>();
                response.put("status", "Success");
                response.put("message", "Libur deleted");
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                Map<String, String> response = new HashMap<>();
                response.put("status", "Failed");
                response.put("message", "Libur not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "Failed");
            response.put("message", "Failed To Delete Libur");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
