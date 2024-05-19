package com.treemaswebapi.treemaswebapi.controller.MasterData.Cuti;

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

import com.treemaswebapi.treemaswebapi.controller.MasterData.Cuti.request.MasterCutiRequest;
import com.treemaswebapi.treemaswebapi.service.MasterData.Cuti.CutiService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/master-data")
@RequiredArgsConstructor
public class CutiController {
    
    private final CutiService service;

    @PostMapping("/cuti-form/add")
    ResponseEntity<Map<String, Object>> cutiAdd(
        @RequestBody MasterCutiRequest request,
        @RequestHeader("Authorization") String jwtToken
    ) {
        ResponseEntity<Map<String, Object>> response = service.cutiAdd(request, jwtToken);
        return response;  
    }

    @GetMapping("/cuti-view")
    public ResponseEntity<Map<String, Object>> cutiGet() {
        ResponseEntity<Map<String,Object>> response = service.cutiGet();
        return response;
    }

    @PutMapping("/cuti-form/edit/{id}")
    public ResponseEntity<Map<String, Object>> cutiUpdate(
        @RequestHeader("Authorization") String jwtToken,
        @RequestBody MasterCutiRequest request,
        @PathVariable String id

    ) {
        ResponseEntity<Map<String, Object>> response = service.cutiUpdate(jwtToken, request, id);
        return response;    
    }

    @DeleteMapping("/cuti-form/delete/{id}")
    public ResponseEntity<Map<String, String>> cutiDelete(
        @PathVariable String id
    ) {
        ResponseEntity<Map<String, String>> response = service.cutiDelete(id);
        return response;
    }
}
