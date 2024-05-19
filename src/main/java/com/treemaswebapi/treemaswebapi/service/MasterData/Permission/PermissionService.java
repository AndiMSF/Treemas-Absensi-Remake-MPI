package com.treemaswebapi.treemaswebapi.service.MasterData.Permission;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.treemaswebapi.treemaswebapi.config.JwtService;
import com.treemaswebapi.treemaswebapi.controller.MasterData.Permission.request.PermissionRequest;
import com.treemaswebapi.treemaswebapi.entity.LiburEntity.LiburEntity;
import com.treemaswebapi.treemaswebapi.entity.PermissionEntity.PermissionEntity;
import com.treemaswebapi.treemaswebapi.repository.KaryawanRepository;
import com.treemaswebapi.treemaswebapi.repository.PermissionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PermissionService {
    private final PermissionRepository permissionRepository;
    private final JwtService jwtService;
    private final KaryawanRepository karyawanRepository;

    public ResponseEntity<Map<String, Object>> permissionAdd(
        PermissionRequest request        
    ) {
        try {

            var permissionEntity = PermissionEntity.builder()
                .namaPermission(request.getNamaPermission())
            .build();

            permissionRepository.save(permissionEntity);
            
            Map<String, Object> data = new HashMap<>();
            data.put("user", permissionEntity);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "Success");
            response.put("message", "Permission Created");
            response.put("data", data);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "Failed");
            response.put("message", "Failed to Create Permission!");
            response.put("error", e.getMessage());
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    public ResponseEntity<Map<String, Object>> permissionGet() {
        try {
            List<PermissionEntity> permission = permissionRepository.findAll();

            Map<String, Object> response = new HashMap<>();
            response.put("status", "Success");
            response.put("message", "Retrieved");
            response.put("data", permission);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "Failed");
            response.put("message", "Failed to retrieve tipe claims");
            response.put("error", e.getMessage());
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    public ResponseEntity<Map<String, Object>> permissionUpdate(
        PermissionRequest request,
        Long id
    ) {
        try {

            Optional<PermissionEntity> permissionOptional = permissionRepository.findById(id);
        
            if (permissionOptional.isPresent()) {
                PermissionEntity newPermission = permissionOptional.get();

                newPermission.setNamaPermission(request.getNamaPermission());

                permissionRepository.save(newPermission);
                
                Map<String, Object> response = new HashMap<>();
                response.put("status", "Success");
                response.put("message", "Permission Updated");
                
                Map<String, Object> data = new HashMap<>();
                data.put("id", newPermission.getId());
                data.put("namaPermission", newPermission.getNamaPermission());

                response.put("data", data);

                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "Failed");
                response.put("message", "Permission Not Found");

                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            }
        
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "Failed");
            response.put("message", "Failed To Update Permission");
            response.put("error", e.getMessage());
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    public ResponseEntity<Map<String, String>> permissionDelete(
        Long id
    ) {
        try {
            // Cari Announcement berdasarkan ID
            Optional<PermissionEntity> permissionOptional = permissionRepository.findById(id);
            if (permissionOptional.isPresent()) {
                permissionRepository.deleteById(id);

                Map<String, String> response = new HashMap<>();
                response.put("status", "Success");
                response.put("message", "Permission deleted");
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                Map<String, String> response = new HashMap<>();
                response.put("status", "Failed");
                response.put("message", "Permission not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "Failed");
            response.put("message", "Failed To Delete Permission");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
