package com.treemaswebapi.treemaswebapi.controller.Management.UserAccess;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.treemaswebapi.treemaswebapi.controller.Management.UserAccess.request.UserAccessRequest;
import com.treemaswebapi.treemaswebapi.service.Management.UserAccess.UserAccessService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/management")
@RequiredArgsConstructor
public class UserAccessController {
    
    private final UserAccessService service;

    @PostMapping("/user-access/add")
    public ResponseEntity<Map<String,Object>> userAccessAdd(
        @RequestBody UserAccessRequest request,
        @RequestHeader("Authorization") String jwtToken
    ) {
        ResponseEntity<Map<String, Object>> response = service.userAccessAdd(request, jwtToken);
        return response;
    }

}
