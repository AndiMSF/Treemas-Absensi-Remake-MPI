package com.treemaswebapi.treemaswebapi.service.MasterData.Jabatan;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import com.treemaswebapi.treemaswebapi.config.JwtService;
import com.treemaswebapi.treemaswebapi.controller.MasterData.Jabatan.request.JabatanRequest;
import com.treemaswebapi.treemaswebapi.entity.JabatanEntity.JabatanEntity;
import com.treemaswebapi.treemaswebapi.entity.KaryawanEntity.KaryawanEntity;
import com.treemaswebapi.treemaswebapi.entity.SysUserEntity.SysUserEntity;
import com.treemaswebapi.treemaswebapi.repository.JabatanRepository;
import com.treemaswebapi.treemaswebapi.repository.KaryawanRepository;
import com.treemaswebapi.treemaswebapi.repository.SysUserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JabatanService {
    private final JabatanRepository jabatanRepository;
    private final KaryawanRepository karyawanRepository;
    private final JwtService jwtService;
    private final SysUserRepository sysUserRepository;

    public ResponseEntity<Map<String, Object>> jabatanAdd(
        JabatanRequest request
    ) {
        try {
        var jabatanEntity = JabatanEntity.builder()
            .jabatanId(request.getJabatanId())
            .namaJabatan(request.getNamaJabatan())
        .build();
        jabatanRepository.save(jabatanEntity);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "Success");
        response.put("message", "Jabatan Created");
        response.put("data", jabatanEntity);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "Failed");
            response.put("message", "Failed To Create Jabatan");
            response.put("error", e.getMessage());
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    public ResponseEntity<Map<String, Object>> jabatanGet() {
        try {
            List<JabatanEntity> jabatan = jabatanRepository.findAll();

            Map<String, Object> response = new HashMap<>();
            response.put("status", "Success");
            response.put("message", "Retrieved");
            response.put("data", jabatan);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "Failed");
            response.put("message", "Failed to retrieve jabatan");
            response.put("error", e.getMessage());
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    public ResponseEntity<Map<String, Object>> jabatanUpdate(
        JabatanRequest request,
        String id,
        @RequestHeader("Authorization") String jwtToken
    ) {
        try {

            // Cari siapa yang akses api ini
            String token = jwtToken.substring(7);
            String userToken = jwtService.extractUsername(token);
            
            Optional<KaryawanEntity> user = karyawanRepository.findByNik(userToken);
            String nama = user.get().getNama();

            Optional<JabatanEntity> jabatanOptional = jabatanRepository.findById(id);
            long currentTimeMillis = System.currentTimeMillis();
            Timestamp dtmUpd = new Timestamp(currentTimeMillis - (currentTimeMillis % 1000));

            if (jabatanOptional.isPresent()) {
                JabatanEntity jabatanEntity = jabatanOptional.get();
                // Hapus entitas lama
                jabatanRepository.delete(jabatanEntity);
                // Buat entitas baru dengan ID baru
                JabatanEntity newJabatanEntity = new JabatanEntity();
                newJabatanEntity.setJabatanId(request.getJabatanId());
                newJabatanEntity.setNamaJabatan(request.getNamaJabatan());
                newJabatanEntity.setUsrUpd(nama);
                newJabatanEntity.setDtmUpd(dtmUpd);
                jabatanRepository.save(newJabatanEntity);

                List<SysUserEntity> userRoles = sysUserRepository.findByRole(id);
                if (!userRoles.isEmpty()) {
                    for (SysUserEntity sysUserEntity : userRoles) {
                        sysUserEntity.setRole(newJabatanEntity);
                        sysUserRepository.save(sysUserEntity);
                    }
                }

            
                Map<String, Object> response = new HashMap<>();
                response.put("status", "Success");
                response.put("message", "Jabatan Updated");
                response.put("data", newJabatanEntity);

                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "Failed");
                response.put("message", "Jabatan Not Found");

                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            }
        
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "Failed");
            response.put("message", "Failed To Create Jabatan");
            response.put("error", e.getMessage());
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    public ResponseEntity<Map<String, String>> jabatanDelete(
        String id
    ) {
        try {
            // Cari Announcement berdasarkan ID
            Optional<JabatanEntity> jabaOptional = jabatanRepository.findById(id);
            if (jabaOptional.isPresent()) {
                jabatanRepository.deleteById(id);

                Map<String, String> response = new HashMap<>();
                response.put("status", "Success");
                response.put("message", "Jabatan deleted");
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                Map<String, String> response = new HashMap<>();
                response.put("status", "Failed");
                response.put("message", "Jabatan not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "Failed");
            response.put("message", "Failed To Delete Jabatan");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
