package com.treemaswebapi.treemaswebapi.service.Management.UserMember;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.treemaswebapi.treemaswebapi.config.JwtService;
import com.treemaswebapi.treemaswebapi.controller.Management.UserMember.request.UserMemberRequest;
import com.treemaswebapi.treemaswebapi.entity.KaryawanEntity.KaryawanEntity;
import com.treemaswebapi.treemaswebapi.entity.SysUserEntity.SysUserEntity;
import com.treemaswebapi.treemaswebapi.entity.SysUserEntity.SysUserMemberEntity;
import com.treemaswebapi.treemaswebapi.repository.KaryawanRepository;
import com.treemaswebapi.treemaswebapi.repository.SysUserMemberRepository;
import com.treemaswebapi.treemaswebapi.repository.SysUserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserMemberService {
    private final SysUserRepository sysUserRepository;
    private final JwtService jwtService;
    private final SysUserMemberRepository sysUserMemberRepository;
    private final KaryawanRepository karyawanRepository;
    
    public ResponseEntity<Map<String, Object>> userMemberAllUser(
        @RequestParam("nikLeader") String nikLeader
    ) {
        try {


            List<SysUserEntity> sysUser = sysUserRepository.findAllByUserIdNot(nikLeader);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "Success");
            response.put("message", "Retrieved");
            response.put("data", sysUser);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "Failed");
            response.put("message", "Failed to retrieve User");
            response.put("error", e.getMessage());
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    public ResponseEntity<Map<String, Object>> userMemberActive(
        @RequestHeader("Authorization") String jwtToken
    ) {
        try {

            // Cari siapa yang akses api ini
            String token = jwtToken.substring(7);
            String userId = jwtService.extractUsername(token);

            List<SysUserMemberEntity> sysUser = sysUserMemberRepository.findAll();
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "Success");
            response.put("message", "Retrieved");
            response.put("data", sysUser);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "Failed");
            response.put("message", "Failed to retrieve User");
            response.put("error", e.getMessage());
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    public ResponseEntity<Map<String, Object>> dropdownUser(
    ) {
        try {
            // Specify the jabatanIds you want to retrieve
            List<String> jabatanIdsToRetrieve = List.of("LEAD", "HEAD");

            List<SysUserEntity> sysUsers = sysUserRepository.findAllByRole_JabatanIdIn(jabatanIdsToRetrieve);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "Success");
            response.put("message", "Retrieved");
            response.put("data", sysUsers);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "Failed");
            response.put("message", "Failed to retrieve User");
            response.put("error", e.getMessage());
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    public ResponseEntity<Map<String, Object>> userMemberAdd(
        @RequestHeader("Authorization") String jwtToken,
        UserMemberRequest request
    ) {
        try {
            // Cari siapa yang akses api ini
            String token = jwtToken.substring(7);
            String nik = jwtService.extractUsername(token); 
            // Cari nikLeader
            String nikLeader = request.getNikLeader();
            Optional<SysUserEntity> searchNikLeader = sysUserRepository.findByUserId(nikLeader);
            
            if (searchNikLeader.isEmpty()) {
                // Handle jika nikLeader tidak ditemukan
                Map<String, Object> response = new HashMap<>();
                response.put("status", "Failed");
                response.put("message", "HEAD or LEAD is not found!");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            // Harus Head
            Optional<SysUserEntity> optionalSysUser = sysUserRepository.findByUserId(nik);
            Optional<KaryawanEntity> karyawan = karyawanRepository.findByNik(nik);
            // Cari siapa yang melakukan aksi
            String namaUser = karyawan.get().getNama();
            // Cari kapan di update
            long currentTimeMillis = System.currentTimeMillis();
            Timestamp dtmUpd = new Timestamp(currentTimeMillis - (currentTimeMillis % 1000));

            if (optionalSysUser.get().getRole() != null && optionalSysUser.get().getRole().getJabatanId().equals("HEAD") || optionalSysUser.get().getRole().getJabatanId().equals("LEAD")) {
                // Buat Entity baru di table sys_user_member
            var sysUserMember = SysUserMemberEntity.builder()
                .nikLeader(request.getNikLeader())
                .nikUser(request.getNikUser())
                .usrUpd(namaUser)
                .dtmUpd(dtmUpd)
            .build();

            sysUserMemberRepository.save(sysUserMember);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "Success");
            response.put("message", "User Added");
            response.put("data", sysUserMember);
            return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "Failed");
                response.put("message", "Not Allowed");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
 
        } catch (Exception e) {
            // TODO: handle exception
            Map<String, Object> response = new HashMap<>();
            response.put("status", "Failed");
            response.put("message", "Failed to add User");
            response.put("error", e.getMessage());
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    public ResponseEntity<Map<String, Object>> dropdownMember(
        String nik
    ) {
        try {
            // Melakukan pencarian data berdasarkan nik
            List<SysUserMemberEntity> users = sysUserMemberRepository.findByNikLeader(nik);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "Success");
            response.put("message", "Retrieved");
            response.put("data", users);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "Failed");
            response.put("message", "Failed to retrieve User");
            response.put("error", e.getMessage());
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @Transactional
    public ResponseEntity<Map<String, Object>> deleteMember(
        String nikLeader,
        String nikUser
    ) {
        try {
            // Periksa apakah data dengan nikUser dan nikLeader yang diberikan ada
            if (!sysUserMemberRepository.existsByNikUserAndNikLeader(nikUser, nikLeader)) {
                // Jika tidak ada data yang cocok, kembalikan respons user deleted already
                Map<String, Object> response = new HashMap<>();
                response.put("status", "Success");
                response.put("message", "User deleted already!");
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
            // Menghapus data berdasarkan nikUser dan nikLeader
            sysUserMemberRepository.deleteByNikUserAndNikLeader(nikUser, nikLeader);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "Success");
            response.put("message", "Deleted");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "Failed");
            response.put("message", "Failed to delete User");
            response.put("error", e.getMessage());
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

}
