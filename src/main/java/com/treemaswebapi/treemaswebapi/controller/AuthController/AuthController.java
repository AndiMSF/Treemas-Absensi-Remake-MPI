package com.treemaswebapi.treemaswebapi.controller.AuthController;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.treemaswebapi.treemaswebapi.service.AuthService.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> authenticate(
        @RequestBody LoginRequest request, boolean isWebAccess
    ) {
        ResponseEntity<Map<String, Object>> response = service.login(request);
        return response;
    }

    @PutMapping("/forgot-password")
    public ResponseEntity<Map<String, Object>> forgetPassword(
        @RequestBody ForgotPasswordRequest request
    ) {
        ResponseEntity<Map<String, Object>> response = service.forgetPassword(request);
        return response;
    }
    
    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(
        @RequestHeader("Authorization") String token
    ) {         
        return service.logout(token);
    }
    

    @PutMapping("/change-password")
    public ResponseEntity<Map<String, Object>> changePassword(
        @RequestHeader("Authorization") String jwtToken,
        @RequestBody ChangePasswordRequest request
    ) {
        ResponseEntity<Map<String, Object>> response = service.changePassword(jwtToken, request);
        return response;
    }
}
