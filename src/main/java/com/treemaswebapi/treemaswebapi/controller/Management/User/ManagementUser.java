package com.treemaswebapi.treemaswebapi.controller.Management.User;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.treemaswebapi.treemaswebapi.service.Management.User.ManagementUserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/management")
@RequiredArgsConstructor
public class ManagementUser {
    
    private final ManagementUserService service;

    @PutMapping("/unlock-account/{nik}")
    public ResponseEntity<Map<String,Object>> unlockAcc(
        @PathVariable String nik
    ) {
        ResponseEntity<Map<String, Object>> response = service.unlockAcc(nik);
        return response;
    }

    @GetMapping("/user-view")
    public ResponseEntity<Map<String, Object>> userGet() {
        ResponseEntity<Map<String,Object>> response = service.userGet();
        return response;
    }
}
