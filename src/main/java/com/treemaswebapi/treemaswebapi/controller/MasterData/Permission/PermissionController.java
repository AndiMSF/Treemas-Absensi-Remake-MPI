package com.treemaswebapi.treemaswebapi.controller.MasterData.Permission;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.treemaswebapi.treemaswebapi.controller.MasterData.Permission.request.PermissionRequest;
import com.treemaswebapi.treemaswebapi.service.MasterData.Permission.PermissionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/master-data")
public class PermissionController {
    
    private final PermissionService service;

    @PostMapping("/permission-form/add")
    ResponseEntity<Map<String, Object>> permissionAdd(
        @RequestBody PermissionRequest request
    ) {
        ResponseEntity<Map<String, Object>> response = service.permissionAdd(request);
        return response;  
    }

    @GetMapping("/permission-view")
    public ResponseEntity<Map<String, Object>> permissionGet() {
        ResponseEntity<Map<String,Object>> response = service.permissionGet();
        return response;
    }

    @PutMapping("/permission-form/edit/{id}")
    ResponseEntity<Map<String, Object>> permissionUpdate(
        @RequestBody PermissionRequest request,
        @PathVariable Long id
    ) {
        ResponseEntity<Map<String, Object>> response = service.permissionUpdate(request, id);
        return response;  
    }

    @DeleteMapping("/permission-form/delete/{id}")
    public ResponseEntity<Map<String, String>> permissionDelete(
        @PathVariable Long id
    ) {
        ResponseEntity<Map<String, String>> response = service.permissionDelete(id);
        return response;
    }
}
