package com.treemaswebapi.treemaswebapi.controller.Parameter.Reimburse;

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

import com.treemaswebapi.treemaswebapi.controller.Parameter.Reimburse.request.ReimburseAddRequest;
import com.treemaswebapi.treemaswebapi.service.Parameter.Reimburse.ReimburseParameterService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/parameter")
@RequiredArgsConstructor
public class ReimburseController {
    private final ReimburseParameterService service;

    @PostMapping("/reimburse-form/add")
    public ResponseEntity<Map<String, Object>> reimburseAdd(
        @RequestBody ReimburseAddRequest request,
        @RequestHeader("Authorization") String jwtToken
    ) {
        ResponseEntity<Map<String, Object>> response = service.reimburseAdd(request, jwtToken);
        return response;
    }

    @PutMapping("/reimburse-form/edit/{id}")
    public ResponseEntity<Map<String, Object>> reimburseUpdate(
        @RequestBody ReimburseAddRequest request,
        @RequestHeader("Authorization") String jwtToken,
        @PathVariable String id
    ) {
        ResponseEntity<Map<String, Object>> response = service.reimburseUpdate(request, jwtToken, id);
        return response;
    }
    
    @GetMapping("/reimburse-view")
    public ResponseEntity<Map<String, Object>> reimburseGet() {
        ResponseEntity<Map<String, Object>> response = service.reimburseGet();
        return response;
    }
}
