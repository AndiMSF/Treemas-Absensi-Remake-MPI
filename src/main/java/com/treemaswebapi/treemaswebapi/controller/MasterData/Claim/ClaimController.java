package com.treemaswebapi.treemaswebapi.controller.MasterData.Claim;

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

import com.treemaswebapi.treemaswebapi.controller.MasterData.Claim.request.TipeClaimRequest;
import com.treemaswebapi.treemaswebapi.service.MasterData.Claim.ClaimService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/master-data")
@RequiredArgsConstructor
public class ClaimController {

    private final ClaimService service;
    
    @PostMapping("/claim-form/add")
    public ResponseEntity<Map<String, Object>> tipeClaimAdd(
        @RequestHeader("Authorization") String jwtToken,
        @RequestBody TipeClaimRequest request

    ) {
        ResponseEntity<Map<String, Object>> response = service.tipeClaimAdd(jwtToken, request);
        return response;    
    }

    @PutMapping("/claim-form/edit/{id}")
    public ResponseEntity<Map<String, Object>> tipeClaimUpdate(
        @RequestHeader("Authorization") String jwtToken,
        @RequestBody TipeClaimRequest request,
        @PathVariable Long id

    ) {
        ResponseEntity<Map<String, Object>> response = service.tipeClaimUpdate(jwtToken, request, id);
        return response;    
    }

    @DeleteMapping("/claim-form/delete/{id}")
    public ResponseEntity<Map<String, String>> tipeClaimDelete(
        @PathVariable Long id
    ) {
        ResponseEntity<Map<String, String>> response = service.tipeClaimDelete(id);
        return response;
    }

    @GetMapping("/claim-view")
    public ResponseEntity<Map<String, Object>> tipeClaimGet() {
        ResponseEntity<Map<String,Object>> response = service.tipeClaimGet();
        return response;
    }
}

