package com.treemaswebapi.treemaswebapi.controller.Management.UserMember;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.treemaswebapi.treemaswebapi.controller.Dashboard.DashboardResponse;
import com.treemaswebapi.treemaswebapi.controller.Management.UserMember.request.UserMemberRequest;
import com.treemaswebapi.treemaswebapi.service.Management.UserMember.UserMemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/management")
@RequiredArgsConstructor
public class UserMemberController {
    private final UserMemberService service;
    
    @GetMapping("/user-member-view")
    public ResponseEntity<Map<String, Object>> userMemberAllUser(
        @RequestParam("nikLeader") String nikLeader
        ) {
        ResponseEntity<Map<String, Object>> response = service.userMemberAllUser(nikLeader);
        return response;
    }

    @GetMapping("/user-member-active")
    public ResponseEntity<Map<String, Object>> userMemberActive(
        @RequestHeader("Authorization") String jwtToken
        ) {
        ResponseEntity<Map<String, Object>> response = service.userMemberActive(jwtToken);
        return response;
    }

    @GetMapping("/user-member-view/dropdown")
    public ResponseEntity<Map<String, Object>> dropdownUser() {
        ResponseEntity<Map<String, Object>> response = service.dropdownUser();
        return response;
    }

    @PostMapping("/user-member-view/add")
    public ResponseEntity<Map<String, Object>> dropdownUser(
        @RequestHeader("Authorization") String jwtToken,
        @RequestBody UserMemberRequest request
    ) {
        ResponseEntity<Map<String, Object>> response = service.userMemberAdd(jwtToken, request);
        return response;
    }

    @GetMapping("/user-member-view/dropdown-member/{nik}")
    public ResponseEntity<Map<String, Object>> dropdownMember(
        @PathVariable String nik
    ) {
        ResponseEntity<Map<String, Object>> response = service.dropdownMember(nik);
        return response;
    }
    
    @DeleteMapping("/user-member-view/delete")
    public ResponseEntity<Map<String, Object>> deleteMember(
        @RequestParam String nikLeader,
        @RequestParam String nikUser
    ) {
        ResponseEntity<Map<String, Object>> response = service.deleteMember(nikLeader, nikUser);
        return response;
    }

} 
