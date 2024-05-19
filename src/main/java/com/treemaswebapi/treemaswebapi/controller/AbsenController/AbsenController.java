package com.treemaswebapi.treemaswebapi.controller.AbsenController;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.treemaswebapi.treemaswebapi.controller.AbsenController.request.AbsenRequest;
import com.treemaswebapi.treemaswebapi.controller.AbsenController.request.UpdatePenempatanReq;
import com.treemaswebapi.treemaswebapi.controller.MemberController.request.MemberRequest;
import com.treemaswebapi.treemaswebapi.service.AbsenService.AbsenService;
import com.treemaswebapi.treemaswebapi.service.AbsenService.MemberService;
import com.treemaswebapi.treemaswebapi.service.UpdateListProjectService.UpdateListProjectService;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;



// api awal buat absen
@RestController
@RequestMapping("/api/absen")
@Data
@RequiredArgsConstructor
public class AbsenController {
    private final MemberService memberService;
    private final AbsenService absenService;
    private final UpdateListProjectService updatePenempatan;

    // buat retrieve project
    @GetMapping("/project-list")
    public ResponseEntity<Map<String, Object>> getProjectDetails(
        @RequestHeader("Authorization") String token
    ) {
        return absenService.getProjectDetails(token);
    }

    // update absen
    @PostMapping("/update-absen")
    public ResponseEntity<Map<String, Object>> updateProject(
        @RequestHeader("Authorization") String token,
        @RequestBody AbsenRequest updateRequest
    ) {
        return absenService.updateAbsen(token, updateRequest);
    }

    // input absenMsk
    @PostMapping("/input-absen")
    public ResponseEntity<Map<String, Object>> inputAbsen(
        @RequestHeader("Authorization") String token,
        @RequestBody AbsenRequest request
    ) {
        return absenService.inputAbsen(token, request);
    }

    // input absenPlg
    @PostMapping("/input-absen-pulang")
    public ResponseEntity<Map<String, Object>> inputAbsenPulang(
        @RequestHeader("Authorization") String token,
        @RequestBody AbsenRequest request
    ) {
        return absenService.inputAbsenPulang(token, request);
    }

    // get absenMsk yang gaada data absenPlg
    @GetMapping("/get-absen-belum-pulang")
    public ResponseEntity<Map<String, Object>> getUnprocessedAbsen(
        @RequestHeader("Authorization") String token
    ){
        return absenService.getAbsenBelumPulang(token);
    }

    @PostMapping("/input-absen-belum-pulang")
    public ResponseEntity<Map<String, Object>> inputAbsenBelumPulang(
        @RequestHeader("Authorization") String token, 
        @RequestParam("id") Long idAbsen,
        @RequestBody AbsenRequest request
    ) {
        return absenService.inputAbsenBelumPulang(token, idAbsen, request);
    }

    // cari data absen by projectId, ntar keluarnya list data absen
    @GetMapping("/get-member")
    public ResponseEntity<Map<String, Object>> getAbsenFromProjectId(
        @RequestHeader("Authorization") String token,
        @RequestBody MemberRequest request
    ){
        return memberService.getAbsenFromProjectId(token, request);
    }

    // fungsi buat si leader untuk retrieve data project yang dia pegang
    @GetMapping("/get-project-details")
    public ResponseEntity<Map<String, Object>> leaderProjectDetails(
        @RequestHeader("Authorization") String token,
        @RequestParam("projectId") String projectIdReq
    ){
        return memberService.leaderProjectDetails(token, projectIdReq);
    }

    // fungsi buat HEAD untuk ambil semua data project yang ada
    @GetMapping("/get-all-projects")
    public ResponseEntity<Map<String,Object>> projectDetails(
        @RequestHeader("Authorization") String token
    ){
        return memberService.projectDetails(token);
    }

    // fungsi buat update alias PUT data penempatan
    @PatchMapping("/update-penempatan")
    public ResponseEntity<Map<String, Object>> updatePenempatan(
        @RequestHeader("Authorization") String token,
        @RequestBody UpdatePenempatanReq request
    ){
        return updatePenempatan.updateProject(token, request);
    }

    // fungsi buat ambil data isAbsen hari ini
    @GetMapping("/get-is-absen")
    public ResponseEntity<Map<String, Object>> getIsAbsen(
        @RequestHeader("Authorization") String token, LocalDate hariIni
    ){
        return absenService.getIsAbsen(token, hariIni);
    }

    @GetMapping("/cek-cuti")
    public ResponseEntity<Map<String, Object>> getAllCuti(
        @RequestHeader("Authorization") String token
    ){
        return absenService.getAllCuti(token);
    }
    @GetMapping("/cek-cuti-by")
    public ResponseEntity<Map<String, Object>> getCutiByDate(
        @RequestHeader("Authorization") String token, @RequestParam("date") String dateString
    ){
        return absenService.getCutiByDate(token, dateString);
    }

    // @GetMapping("/get-all-members")
    // public ResponseEntity<Map<String, Object>> headMemberDetails(
    //     @RequestHeader("Authorization") String token
    // ){
    //     return memberService.head
    // }

    @PutMapping("/masukinnamakeabsen")
    public ResponseEntity<Map<String, Object>> masukinNama(@RequestHeader("Authorization") String tokenWithBearer) {
        //TODO: process PUT request
        
        return absenService.masukinNama(tokenWithBearer);
    }
}   