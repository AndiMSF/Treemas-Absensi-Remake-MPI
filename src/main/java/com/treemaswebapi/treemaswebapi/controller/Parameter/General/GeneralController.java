package com.treemaswebapi.treemaswebapi.controller.Parameter.General;

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

import com.treemaswebapi.treemaswebapi.controller.Parameter.General.request.GeneralAddRequest;
import com.treemaswebapi.treemaswebapi.controller.Parameter.Reimburse.request.ReimburseAddRequest;
import com.treemaswebapi.treemaswebapi.service.Parameter.General.GeneralService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/parameter")
@RequiredArgsConstructor
public class GeneralController {
    private final GeneralService service;

    @PostMapping("/general-form/add")
    public ResponseEntity<Map<String, Object>> generalAdd(
        @RequestBody GeneralAddRequest request
    ) {
        ResponseEntity<Map<String, Object>> response = service.generalAdd(request);
        return response;
    }

    @PutMapping("/general-form/edit/{id}")
    public ResponseEntity<Map<String, Object>> generalUpdate(
        @RequestBody GeneralAddRequest request,
        @RequestHeader("Authorization") String jwtToken,
        @PathVariable String id
    ) {
        ResponseEntity<Map<String, Object>> response = service.generalUpdate(request, jwtToken, id);
        return response;
    }
    
    @GetMapping("/general-view")
    public ResponseEntity<Map<String, Object>> generalGet() {
        ResponseEntity<Map<String, Object>> response = service.generalGet();
        return response;
    }
}
