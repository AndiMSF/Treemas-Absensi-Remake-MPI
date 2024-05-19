package com.treemaswebapi.treemaswebapi.controller.DetailData.Tracking;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.treemaswebapi.treemaswebapi.service.DetailData.Tracking.TrackingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/detail-data")
@RequiredArgsConstructor
public class TrackingController {

    private final TrackingService service;
    
    @GetMapping("/tracking-view")
    public ResponseEntity<Map<String, Object>> trackingView(
        @RequestHeader("Authorization") String jwtToken,
        @RequestParam(required =  false) String nik,
        @RequestParam(required = false) String startDate,
        @RequestParam(required = false) String endDate
        ) {
        ResponseEntity<Map<String, Object>> response = service.trackingView(jwtToken, nik, startDate, endDate);
        return response;
    } 

}
