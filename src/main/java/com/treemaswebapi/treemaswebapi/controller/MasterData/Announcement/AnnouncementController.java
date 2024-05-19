package com.treemaswebapi.treemaswebapi.controller.MasterData.Announcement;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.treemaswebapi.treemaswebapi.controller.MasterData.Announcement.request.AnnouncementRequest;
import com.treemaswebapi.treemaswebapi.service.MasterData.Announcement.AnnouncementService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/master-data")
public class AnnouncementController {
    
    private final AnnouncementService service;

    @PostMapping("/announcement-form/add")
    public ResponseEntity<Map<String, Object>> announcementAdd(
        @RequestBody AnnouncementRequest request,
        @RequestHeader("Authorization") String jwtToken
    ) {
        ResponseEntity<Map<String, Object>> response = service.announcementAdd(request, jwtToken);
        return response;
    }

    @GetMapping("/announcement-view")
    public ResponseEntity<Map<String, Object>> announcementGet(
    ) {
        ResponseEntity<Map<String, Object>> response = service.announcementGet();
        return response;
    }

    @GetMapping("/announcement-view/{id}")
    public ResponseEntity<Map<String, Object>> announcementGetId(
        @PathVariable Long id
    ) {
        ResponseEntity<Map<String, Object>> response = service.announcementGetId(id);
        return response;
    }

    @PutMapping("/announcement-form/edit/{id}")
    public ResponseEntity<Map<String, Object>> announcementUpdate(
        @PathVariable Long id, 
        @RequestBody AnnouncementRequest request,
        @RequestHeader("Authorization") String jwtToken
        ) {
        ResponseEntity<Map<String, Object>> response = service.announcementUpdate(id,request,jwtToken);
        return response;
    }

    @DeleteMapping("/announcement-form/delete/{id}")
    public ResponseEntity<Map<String, String>> announcementDelete(
        @PathVariable Long id
    ) {
        ResponseEntity<Map<String, String>> response = service.announcementDelete(id);
        return response;
    }
}
