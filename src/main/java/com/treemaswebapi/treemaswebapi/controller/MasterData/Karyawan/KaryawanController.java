package com.treemaswebapi.treemaswebapi.controller.MasterData.Karyawan;

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

import com.treemaswebapi.treemaswebapi.controller.MasterData.Karyawan.request.KaryawanAddRequest;
import com.treemaswebapi.treemaswebapi.service.MasterData.Karyawan.KaryawanService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/master-data")
public class KaryawanController {
    
    private final KaryawanService service;

    @PostMapping("/karyawan-form/add")
    public ResponseEntity<Map<String, String>> karyawanAdd(
        @RequestBody KaryawanAddRequest request,
        @RequestHeader("Authorization") String jwtToken
    ) {
        ResponseEntity<Map<String, String>> response = service.karyawanAdd(request, jwtToken);
        return response;
    }

    @GetMapping("/karyawan-view")
    public ResponseEntity<Map<String, Object>> karyawanGet() {
        ResponseEntity<Map<String,Object>> response = service.karyawanGet();
        return response;
    }

    @GetMapping("/karyawan-view/{id}")
    public ResponseEntity<Map<String, Object>> karyawanGetId(
        @PathVariable String id
    ) {
        ResponseEntity<Map<String, Object>> response = service.karyawanGetId(id);
        return response;
    }


    @DeleteMapping("/karyawan-form/delete/{id}")
    public ResponseEntity<Map<String, String>> karyawanDelete(
        @PathVariable String id
    ) {
        ResponseEntity<Map<String, String>> response = service.karyawanDelete(id);
        return response;
    }

    @PutMapping("/karyawan-form/edit/{id}")
    public ResponseEntity<Map<String, String>> karyawanEdit(
        @PathVariable String id,
        @RequestHeader("Authorization") String jwtToken,
        @RequestBody KaryawanAddRequest request
    ) {
        ResponseEntity<Map<String, String>> response = service.karyawanEdit(id, jwtToken, request);
        return response;
    }
}
