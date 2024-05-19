package com.treemaswebapi.treemaswebapi.controller.MasterData.Project;

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

import com.treemaswebapi.treemaswebapi.controller.MasterData.Project.request.ProjectRequest;
import com.treemaswebapi.treemaswebapi.service.MasterData.Project.ProjectService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/master-data")
public class ProjectController {
    
    private final ProjectService service;

    @PostMapping("/project-form/add")
    ResponseEntity<Map<String, Object>> projectAdd(
        @RequestBody ProjectRequest request
    ) {
        ResponseEntity<Map<String, Object>> response = service.projectAdd(request);
        return response;  
    }

    @GetMapping("/project-view")
    ResponseEntity<Map<String, Object>> projectGet() {
        ResponseEntity<Map<String, Object>> response = service.projectGet();
        return response;  
    }

    @PutMapping("/project-form/edit/{id}")
    ResponseEntity<Map<String, Object>> projectUpdate(
        @RequestBody ProjectRequest request,
        @PathVariable String id,
        @RequestHeader("Authorization") String jwtToken
    ) {
        ResponseEntity<Map<String, Object>> response = service.projectUpdate(request, id, jwtToken);
        return response;  
    }

    @DeleteMapping("/project-form/delete/{id}")
    public ResponseEntity<Map<String, String>> projectDelete(
        @PathVariable String id
    ) {
        ResponseEntity<Map<String, String>> response = service.projectDelete(id);
        return response;
    }
}
