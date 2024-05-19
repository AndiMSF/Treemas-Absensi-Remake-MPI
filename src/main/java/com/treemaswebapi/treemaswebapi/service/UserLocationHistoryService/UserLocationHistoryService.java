package com.treemaswebapi.treemaswebapi.service.UserLocationHistoryService;

import java.util.Map;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.http.HttpStatus;

import com.treemaswebapi.treemaswebapi.config.JwtService;
import com.treemaswebapi.treemaswebapi.controller.TrackingKaryawanController.request.TrackingKaryawanRequest;
import com.treemaswebapi.treemaswebapi.entity.UserLocationHistoryEntity.UserLocationHistoryEntity;
import com.treemaswebapi.treemaswebapi.repository.UserLocationHistoryRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class UserLocationHistoryService {
    
    private final JwtService jwtService;
    private final UserLocationHistoryRepository userLocationHistoryRepository;

    public ResponseEntity<Map<String, Object>> postLocationHistory(@RequestHeader("Authorization") String tokenWithBearer, @RequestBody TrackingKaryawanRequest trackingKaryawanRequest){
        try{
            Map<String, Object> response = new HashMap<>();
            if (tokenWithBearer.startsWith("Bearer ")) {
                    String token = tokenWithBearer.substring("Bearer ".length());
                    String nik = jwtService.extractUsername(token);
                    UserLocationHistoryEntity userLocation = new UserLocationHistoryEntity();
                    userLocation.setAccuracy(trackingKaryawanRequest.getAccuracy());
                    System.out.println("ACCURACY YANG MASUK "+ trackingKaryawanRequest.getAccuracy());
                    userLocation.setUserId(nik);
                    userLocation.setLatitude(trackingKaryawanRequest.getLatitude());
                    System.out.println("LATITUDE YANG MASUK "+ trackingKaryawanRequest.getLatitude());
                    userLocation.setLongitude(trackingKaryawanRequest.getLongitude());
                    System.out.println("LONGITUDE YANG MASUK "+ trackingKaryawanRequest.getLongitude());
                    userLocation.setDtmcrt(Timestamp.valueOf(LocalDateTime.now()));
                    userLocationHistoryRepository.save(userLocation);

                    response.put("success",true);
                    response.put("message","berhasil menambahkan lokasi ke database");
                    response.put("data",userLocation);
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                }else{
                    response.put("success", false);
                    response.put("message", "Invalid token format");
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
                }
            } catch (Exception e) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "error");
                response.put("error", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }

    }
}
