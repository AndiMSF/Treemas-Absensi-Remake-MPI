package com.treemaswebapi.treemaswebapi.controller.MasterData.Jabatan;

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

import com.treemaswebapi.treemaswebapi.controller.MasterData.Jabatan.request.JabatanRequest;
import com.treemaswebapi.treemaswebapi.service.MasterData.Jabatan.JabatanService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/master-data")
@RequiredArgsConstructor
public class JabatanController {
    
    private final JabatanService service;

    @PostMapping("/jabatan-form/add")
    ResponseEntity<Map<String, Object>> jabatanAdd(
        @RequestBody JabatanRequest request
    ) {
        ResponseEntity<Map<String, Object>> response = service.jabatanAdd(request);
        return response;  
    }

    @PutMapping("/jabatan-form/edit/{id}")
    ResponseEntity<Map<String, Object>> jabatanUpdate(
        @RequestBody JabatanRequest request,
        @PathVariable String id,
        @RequestHeader("Authorization") String jwtToken
    ) {
        ResponseEntity<Map<String, Object>> response = service.jabatanUpdate(request, id, jwtToken);
        return response;  
    }

    @GetMapping("/jabatan-view")
    public ResponseEntity<Map<String, Object>> jabatanGet() {
        ResponseEntity<Map<String,Object>> response = service.jabatanGet();
        return response;
    }

    @DeleteMapping("/jabatan-form/delete/{id}")
    public ResponseEntity<Map<String, String>> jabatanDelete(
        @PathVariable String id
    ) {
        ResponseEntity<Map<String, String>> response = service.jabatanDelete(id);
        return response;
    }
}
