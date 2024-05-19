package com.treemaswebapi.treemaswebapi.controller.ProfileController;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.treemaswebapi.treemaswebapi.controller.ProfileController.request.ProfileRequest;
import com.treemaswebapi.treemaswebapi.service.ProfileService.ProfileService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService service;
    
    @PutMapping("/update-profile")
    public ResponseEntity<Map<String, String>> updateProfile(
       @RequestBody ProfileRequest request,
       @RequestHeader("Authorization") String jwtToken
    ) {
        ResponseEntity<Map<String, String>> response = service.updateProfile(request, jwtToken);
        return response;    
    }

    @PutMapping("/update-profile-mobile")
    public ResponseEntity<Map<String, String>> updateProfileMobile(
       @RequestBody ProfileRequest request,
       @RequestHeader("Authorization") String jwtToken
    ) {
        ResponseEntity<Map<String, String>> response = service.updateProfileMobile(request, jwtToken);
        return response;    
    }
}
