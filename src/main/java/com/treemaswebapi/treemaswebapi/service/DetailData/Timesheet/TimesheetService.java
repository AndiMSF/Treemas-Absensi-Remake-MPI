package com.treemaswebapi.treemaswebapi.service.DetailData.Timesheet;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
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
import com.treemaswebapi.treemaswebapi.entity.TimesheetEntity.TimesheetEntity;
import com.treemaswebapi.treemaswebapi.entity.KaryawanEntity.KaryawanEntity;
import com.treemaswebapi.treemaswebapi.entity.ProjectEntity.ProjectEntity;
import com.treemaswebapi.treemaswebapi.entity.ReimburseEntity.ReimburseAppEntity;
import com.treemaswebapi.treemaswebapi.repository.KaryawanRepository;
import com.treemaswebapi.treemaswebapi.repository.ReimburseAppRepository;
import com.treemaswebapi.treemaswebapi.repository.TimesheetRepository;
import com.treemaswebapi.treemaswebapi.service.RekapService.ReimburseResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TimesheetService {
    private final TimesheetRepository timesheetRepository;
    private final KaryawanRepository karyawanRepository;
    private final JwtService jwtService;
    private final ReimburseAppRepository reimburseAppRepository;
    
    // Get Data Timesheet ( MEMBER = tapi masih belum jadi fitur member add nya di web sementara findAll di table reimburseApp )
    public ResponseEntity<Map<String, Object>> timesheetGet(
        @RequestHeader("Authorization") String jwtToken        ) {
            try {
                if (jwtToken.startsWith("Bearer ")) {
                    String token = jwtToken.substring("Bearer ".length());
                    String nik = jwtService.extractUsername(token);
        
                    List<TimesheetEntity> dataTimesheetnya = timesheetRepository.findAll();
        
                    if (!dataTimesheetnya.isEmpty()) {
                        Map<String, Object> response = new HashMap<>();
                        response.put("success", true);
                        response.put("message", "Data Timesheet retrieved successfully");
                        response.put("data", dataTimesheetnya);
                        return ResponseEntity.status(HttpStatus.OK).body(response);
                    } else {
                        Map<String, Object> response = new HashMap<>();
                        response.put("success", false);
                        response.put("message", "No Data Timesheet found for nik :" + nik);
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                    }
                } else {
                    // Handle the case where the token format is invalid
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", false);
                    response.put("message", "Invalid token format");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                }
            } catch (Exception e) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Failed to retrieve Data Timesheet");
                response.put("error", e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
    }

    /* --------------------------------------------BAGIAN TIMESHEET------------------------------------------------ */
    public ResponseEntity<Map<String, Object>> rekapTimesheet(@RequestHeader String tokenWithBearer) {
        try {
            if (tokenWithBearer.startsWith("Bearer ")) {
                String token = tokenWithBearer.substring("Bearer ".length());
                String nik = jwtService.extractUsername(token);
    
                List<TimesheetEntity> dataTimesheetnya = timesheetRepository.findAllByNik(nik);
    
                if (!dataTimesheetnya.isEmpty()) {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("message", "Data Timesheet for nik: "+nik+" retrieved successfully");
                    response.put("data", dataTimesheetnya);
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                } else {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", false);
                    response.put("message", "No Data Timesheet found for nik :" + nik);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                }
            } else {
                // Handle the case where the token format is invalid
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Invalid token format");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to retrieve Data Timesheet");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
