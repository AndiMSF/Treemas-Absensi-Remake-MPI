package com.treemaswebapi.treemaswebapi.controller.MemberController;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.treemaswebapi.treemaswebapi.entity.ProjectEntity.ProjectEntity;
import com.treemaswebapi.treemaswebapi.service.AbsenService.AbsenService;
import com.treemaswebapi.treemaswebapi.service.AbsenService.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final AbsenService absenService;

    @GetMapping("/get-project")
    public ResponseEntity<Map<String, Object>> getProject(
        @RequestHeader("Authorization") String tokenWithBearer
    ){
        return memberService.getProject(tokenWithBearer);
    }

    @GetMapping("/get-member-project")
    public ResponseEntity<Map<String, Object>> getProjectDetails(
        @RequestHeader("Authorization") String tokenWithBearer,
        @RequestParam("projectId") ProjectEntity projectId
    ) {
        return memberService.getAbsenMemberByProjectId(tokenWithBearer, projectId);
    }

    @GetMapping("/get-data-absen")
    public ResponseEntity<Map<String, Object>> getDataAbsen(
        @RequestHeader("Authorization") String tokenWithBearer,
        @RequestParam("nik") String nik
    ) {
        return memberService.getDataAbsenMember(tokenWithBearer, nik);
    }
}
