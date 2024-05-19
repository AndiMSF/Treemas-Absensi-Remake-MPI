package com.treemaswebapi.treemaswebapi.service.Parameter.Reimburse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.treemaswebapi.treemaswebapi.config.JwtService;
import com.treemaswebapi.treemaswebapi.controller.Parameter.Reimburse.request.ReimburseAddRequest;
import com.treemaswebapi.treemaswebapi.entity.KaryawanEntity.KaryawanEntity;
import com.treemaswebapi.treemaswebapi.entity.ReimburseEntity.ReimburseParamEntity;
import com.treemaswebapi.treemaswebapi.repository.KaryawanRepository;
import com.treemaswebapi.treemaswebapi.repository.ReimburseParamRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReimburseParameterService {
    
    private final JwtService jwtService;
    private final ReimburseParamRepository reimburseParamRepository;
    private final KaryawanRepository karyawanRepository;

    public ResponseEntity<Map<String, Object>> reimburseAdd(
        ReimburseAddRequest request,
        @RequestHeader("Authorization") String jwtToken
    ) {
        try {
            // Cari siapa yang akses api ini
            String token = jwtToken.substring(7);
            String userToken = jwtService.extractUsername(token);
            
            Optional<KaryawanEntity> user = karyawanRepository.findByNik(userToken);
            String isLeader = user.get().getIsLeader();
            var reimburseParamEntity = ReimburseParamEntity.builder()
                .reimburseId(request.getReimburseId())
                .nama(request.getNama())
                .nominal(request.getNominal())
                .note(request.getNote())
                .isLeader(isLeader)
            .build();

            reimburseParamRepository.save(reimburseParamEntity);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "Success");
            response.put("message", "Reimburse Created");

            Map<String, Object> data = new HashMap<>();
            data.put("reimburseId", reimburseParamEntity.getReimburseId());
            data.put("namaReimburse", reimburseParamEntity.getNama());
            data.put("nominal", reimburseParamEntity.getNominal());
            data.put("note", reimburseParamEntity.getNote());
            data.put("isLeader", reimburseParamEntity.getIsLeader());
            
            response.put("data", data);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "Failed");
            response.put("message", "Failed To Create Reimburse");
            response.put("error", e.getMessage());
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    public ResponseEntity<Map<String, Object>> reimburseUpdate(
        @RequestBody ReimburseAddRequest request,
        @RequestHeader("Authorization") String jwtToken,
        String id
    ) {
        try {
            // Cari Reimburse Param berdasarkan ID
            Optional<ReimburseParamEntity> reimburseParamOptional = reimburseParamRepository.findById(id);
            if (reimburseParamOptional.isPresent()) {
                ReimburseParamEntity updReimburseParam = reimburseParamOptional.get();

                updReimburseParam.setNama(request.getNama());
                updReimburseParam.setNominal(request.getNominal());
                updReimburseParam.setNote(request.getNote());
                reimburseParamRepository.save(updReimburseParam);

                Map<String, Object> response = new HashMap<>();
                response.put("status", "Success");
                response.put("message", "Reimburse Updated");

                Map<String, Object> data = new HashMap<>();
                data.put("reimburseId", updReimburseParam.getReimburseId());
                data.put("namaReimburse", updReimburseParam.getNama());
                data.put("nominal", updReimburseParam.getNominal());
                data.put("note", updReimburseParam.getNote());
                data.put("isLeader", updReimburseParam.getIsLeader());
                
                response.put("data", data);

                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "Failed");
                response.put("message", "Reimburse Not Found");

                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "Failed");
            response.put("message", "Failed To Update Reimburse");
            response.put("error", e.getMessage());
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        
    }

    public ResponseEntity<Map<String, Object>> reimburseGet() {
        try {
            List<ReimburseParamEntity> reimburseParam = reimburseParamRepository.findAll();

            Map<String, Object> response = new HashMap<>();
            response.put("status", "Success");
            response.put("message", "Retrieved");
            response.put("data", reimburseParam);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "Failed");
            response.put("message", "Failed to retrieve tipe reimburse");
            response.put("error", e.getMessage());
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
