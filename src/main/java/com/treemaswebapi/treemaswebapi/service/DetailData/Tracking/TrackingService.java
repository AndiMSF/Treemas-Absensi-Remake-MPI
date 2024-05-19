package com.treemaswebapi.treemaswebapi.service.DetailData.Tracking;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import com.treemaswebapi.treemaswebapi.entity.UserLocationHistoryEntity.UserLocationHistoryEntity;
import com.treemaswebapi.treemaswebapi.repository.UserLocationHistoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TrackingService {

    private final UserLocationHistoryRepository userLocationHistoryRepository;
    
    public ResponseEntity<Map<String, Object>> trackingView(
        @RequestHeader("Authorization") String jwtToken,
        String nik, 
        String startDate, 
        String endDate
) {
    try {
        // Konversi tanggal mulai dan akhir menjadi format yang sesuai
        LocalDateTime startDateTime = LocalDateTime.parse(startDate + "T00:00:00");
        LocalDateTime endDateTime = LocalDateTime.parse(endDate + "T23:59:59");

        // Konversi LocalDateTime ke timestamp
        long startTimestamp = startDateTime.atZone(ZoneId.systemDefault()).toEpochSecond();
        long endTimestamp = endDateTime.atZone(ZoneId.systemDefault()).toEpochSecond();
        
        // Cari range tanggal dan userId / nik
        List<UserLocationHistoryEntity> userLocationHistoryList = userLocationHistoryRepository.findByUserIdAndDtmcrtBetween(nik, new Timestamp(startTimestamp * 1000), new Timestamp(endTimestamp * 1000));

        Map<String, Object> response = new HashMap<>();

        if (userLocationHistoryList.isEmpty()) {
            response.put("success", false);
            response.put("message", "User ID not found or data not found for the specified date range");
        } else {
            response.put("success", true);
            response.put("message", "Tracking data retrieved");
            response.put("data", userLocationHistoryList);
        }

        return ResponseEntity.status(HttpStatus.OK).body(response);  
    } catch (Exception e) {
        // TODO: handle exception
        Map<String, Object> response = new HashMap<>();
        response.put("status", "Failed");
        response.put("message", "Failed to retrieve tracking user");
        response.put("error", e.getMessage());
        System.out.println(e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}

}
