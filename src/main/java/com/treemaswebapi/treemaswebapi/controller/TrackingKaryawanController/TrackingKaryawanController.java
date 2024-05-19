package com.treemaswebapi.treemaswebapi.controller.TrackingKaryawanController;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.treemaswebapi.treemaswebapi.controller.TrackingKaryawanController.request.TrackingKaryawanRequest;
import com.treemaswebapi.treemaswebapi.service.UserLocationHistoryService.UserLocationHistoryService;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tracking")
@Data
@RequiredArgsConstructor
public class TrackingKaryawanController {
    private final UserLocationHistoryService locationHistoryService;

    @PostMapping("/post-location-history")
    public ResponseEntity<Map<String, Object>> postLocationHistory(
        @RequestHeader("Authorization") String token, 
        @RequestBody TrackingKaryawanRequest trackingKaryawanRequest
    ) {
        return locationHistoryService.postLocationHistory(token, trackingKaryawanRequest);
    }
}