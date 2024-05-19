package com.treemaswebapi.treemaswebapi.controller.DetailData.CutiSakit;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.treemaswebapi.treemaswebapi.controller.DetailData.CutiSakit.request.CutiApprove;
import com.treemaswebapi.treemaswebapi.controller.DetailData.CutiSakit.request.CutiRequest;
import com.treemaswebapi.treemaswebapi.controller.DetailData.CutiSakit.request.SakitRequest;
import com.treemaswebapi.treemaswebapi.service.DetailData.CutiSakit.CutiSakitService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/detail-data")
@RequiredArgsConstructor
public class CutiSakitController {
    private final CutiSakitService service;

    @GetMapping("/cuti-view")
    public ResponseEntity<Map<String, Object>> cutiGet() {
        ResponseEntity<Map<String, Object>> response = service.cutiGet();
        return response;
    }

    @GetMapping("/sakit-view")
    public ResponseEntity<Map<String, Object>> sakitGet() {
        ResponseEntity<Map<String, Object>> response = service.sakitGet();
        return response;
    }
    // ini ke bawah bikinan andi
    @PostMapping("/cuti-form/add")
    public ResponseEntity<Map<String, Object>> userCutiAdd(
        @RequestHeader("Authorization") String jwtToken,
        @RequestBody CutiRequest request
    ) {
        ResponseEntity<Map<String, Object>> response = service.userCutiAdd(jwtToken, request);
        return response;
    }
    
    @GetMapping("/cuti-get")
    public ResponseEntity<Map<String, Object>> getMyCuti(
        @RequestHeader("Authorization") String tokenWithBearer
    ){
        ResponseEntity<Map<String, Object>> response = service.getMyCuti(tokenWithBearer);
        return response;
    }

    @PutMapping("/cuti-form/approve/{id}")
    public ResponseEntity<Map<String, Object>> cutiApprove(
        @RequestHeader("Authorization") String jwtToken,
        @RequestBody CutiApprove request,
        @PathVariable Long id
    ) {
        ResponseEntity<Map<String, Object>> response = service.cutiApprove(jwtToken, request, id);
        return response;
    }

    @PostMapping("/sakit-form/add")
    public ResponseEntity<Map<String, Object>> userSakitAdd(
        @RequestHeader("Authorization") String jwtToken,
        @RequestBody SakitRequest request
    ) {
        ResponseEntity<Map<String, Object>> response = service.userSakitAdd(jwtToken, request);
        return response;
    }

    @PutMapping("/sakit-form/approve/{id}")
    public ResponseEntity<Map<String, Object>> sakitApprove(
        @RequestHeader("Authorization") String jwtToken,
        @RequestBody CutiApprove request,
        @PathVariable Long id
    ) {
        ResponseEntity<Map<String, Object>> response = service.sakitApprove(jwtToken, request, id);
        return response;
    }

        
}
