package com.treemaswebapi.treemaswebapi.service.Parameter.General;

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
import com.treemaswebapi.treemaswebapi.controller.Parameter.General.request.GeneralAddRequest;
import com.treemaswebapi.treemaswebapi.entity.GeneralEntity.GeneralParamEntity;
import com.treemaswebapi.treemaswebapi.entity.KaryawanEntity.KaryawanEntity;
import com.treemaswebapi.treemaswebapi.repository.GeneralParamRepository;
import com.treemaswebapi.treemaswebapi.repository.KaryawanRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GeneralService {
    private final JwtService jwtService;
    private final KaryawanRepository karyawanRepository;
    private final GeneralParamRepository generalParamRepository;

    public ResponseEntity<Map<String, Object>> generalAdd(
        GeneralAddRequest request
    ) {
        try {
            // Cari siapa yang akses api ini

            var generalParamEntity = GeneralParamEntity.builder()
                .id(request.getId())
                .dataType(request.getDataType())
                .val(request.getVal())
                .paramDesc(request.getParamDesc())
                .isVisible(request.getIsVisible()) // untuk tampil di websitenya
            .build();

            generalParamRepository.save(generalParamEntity);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "Success");
            response.put("message", "General Created");

            Map<String, Object> data = new HashMap<>();
            data.put("id", generalParamEntity.getId());
            data.put("dataType", generalParamEntity.getDataType());
            data.put("value", generalParamEntity.getVal());
            data.put("paramDesc", generalParamEntity.getParamDesc());
            data.put("isVisible", generalParamEntity.getIsVisible());
            response.put("data", data);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "Failed");
            response.put("message", "Failed To Create General");
            response.put("error", e.getMessage());
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    public ResponseEntity<Map<String, Object>> generalUpdate(
        GeneralAddRequest request,
        @RequestHeader("Authorization") String jwtToken,
        String id
    ) {
        try {
            // Cari siapa yang akses api ini
            String token = jwtToken.substring(7);
            String userToken = jwtService.extractUsername(token);
            
            Optional<KaryawanEntity> user = karyawanRepository.findByNik(userToken);
            String nama = user.get().getNama();

            Optional<GeneralParamEntity> generalParamOptional = generalParamRepository.findById(id);
            long currentTimeMillis = System.currentTimeMillis();
            Timestamp dtmUpd = new Timestamp(currentTimeMillis - (currentTimeMillis % 1000));
            if (generalParamOptional.isPresent()) {
                GeneralParamEntity updateGeneralParam = generalParamOptional.get();

                updateGeneralParam.setDataType(request.getDataType());
                updateGeneralParam.setVal(request.getVal());
                updateGeneralParam.setParamDesc(request.getParamDesc());
                updateGeneralParam.setUsrUpd(nama);
                updateGeneralParam.setDtmUpd(dtmUpd);
                generalParamRepository.save(updateGeneralParam);
                
                Map<String, Object> response = new HashMap<>();
                response.put("status", "Success");
                response.put("message", "General Updated");
                
                Map<String, Object> data = new HashMap<>();
                data.put("id", updateGeneralParam.getId());
                data.put("dataType", updateGeneralParam.getDataType());
                data.put("value", updateGeneralParam.getVal());
                data.put("paramDesc", updateGeneralParam.getParamDesc());
                data.put("usrUpd", updateGeneralParam.getUsrUpd());
                data.put("dtmUpd", updateGeneralParam.getDtmUpd());

                response.put("data", data);

                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "Failed");
                response.put("message", "General Not Found");

                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            }
        
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "Failed");
            response.put("message", "Failed To Update General");
            response.put("error", e.getMessage());
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    public ResponseEntity<Map<String, Object>> generalGet() {
        try {
            List<GeneralParamEntity> generalParam = generalParamRepository.findAll();

            Map<String, Object> response = new HashMap<>();
            response.put("status", "Success");
            response.put("message", "Retrieved");
            response.put("data", generalParam);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "Failed");
            response.put("message", "Failed to retrieve tipe general");
            response.put("error", e.getMessage());
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
