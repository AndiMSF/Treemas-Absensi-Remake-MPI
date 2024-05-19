package com.treemaswebapi.treemaswebapi.service.Management.UserAccess;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.treemaswebapi.treemaswebapi.config.JwtService;
import com.treemaswebapi.treemaswebapi.controller.Management.UserAccess.request.UserAccessRequest;
import com.treemaswebapi.treemaswebapi.entity.MenuEntity.MenuEntity;
import com.treemaswebapi.treemaswebapi.entity.SysUserEntity.SysUserEntity;
import com.treemaswebapi.treemaswebapi.repository.SysUserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserAccessService {
    private final SysUserRepository sysUserRepository;
    private final JwtService jwtService;

    public ResponseEntity<Map<String, Object>> userAccessAdd(
        @RequestBody UserAccessRequest request,
        @RequestHeader("Authorization") String jwtToken
    ) {
        try {

            // Cari siapa yang akses api ini
            String token = jwtToken.substring(7);
            String userId = jwtService.extractUsername(token);

            // Cari nama
            Optional<SysUserEntity> sysUser = sysUserRepository.findByUserId(userId);
            String namaUserToken = sysUser.get().getFullName();

            // Cari kapan di update
            long currentTimeMillis = System.currentTimeMillis();
            Timestamp dtmUpd = new Timestamp(currentTimeMillis - (currentTimeMillis % 1000));

            var menuEntity = MenuEntity.builder()
                .jabatan(request.getJabatan())
                .child(request.getChild())
                .parent(request.getParent())
                .usrUpd(namaUserToken)
                .dtmUpd(dtmUpd)
            .build();
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "Success");
            response.put("message", "Access Added!");
            response.put("data", menuEntity);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "Failed");
            response.put("message", "Failed To Add Access!");
            response.put("error", e.getMessage());
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
