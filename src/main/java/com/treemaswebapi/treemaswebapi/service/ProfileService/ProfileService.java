package com.treemaswebapi.treemaswebapi.service.ProfileService;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.treemaswebapi.treemaswebapi.config.JwtService;
import com.treemaswebapi.treemaswebapi.controller.ProfileController.request.ProfileRequest;
import com.treemaswebapi.treemaswebapi.entity.JabatanEntity.JabatanEntity;
import com.treemaswebapi.treemaswebapi.entity.KaryawanEntity.KaryawanEntity;
import com.treemaswebapi.treemaswebapi.entity.KaryawanEntity.KaryawanImageEntity;
import com.treemaswebapi.treemaswebapi.entity.ProjectEntity.ProjectEntity;
import com.treemaswebapi.treemaswebapi.entity.SysUserEntity.SysUserEntity;
import com.treemaswebapi.treemaswebapi.entity.UserRole.Role;
import com.treemaswebapi.treemaswebapi.repository.JabatanRepository;
import com.treemaswebapi.treemaswebapi.repository.KaryawanImageRepository;
import com.treemaswebapi.treemaswebapi.repository.KaryawanRepository;
import com.treemaswebapi.treemaswebapi.repository.ProjectRepository;
import com.treemaswebapi.treemaswebapi.repository.SysUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileService {
    
    private final KaryawanRepository karyawanRepository;
    private final KaryawanImageRepository karyawanImageRepository;
    private final SysUserRepository sysUserRepository;
    private final JwtService jwtService;
    private final JabatanRepository jabatanRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final ProjectRepository projectRepository;

    public ResponseEntity<Map<String, String>> updateProfile(
        ProfileRequest request, 
        @RequestHeader("Authorization") String jwtToken
        ) {
        try {

            // Cari siapa yang akses api ini
            String token = jwtToken.substring(7);
            String userToken = jwtService.extractUsername(token);
            
            System.out.println("INI TOKEN : "+token);
            System.out.println("INI USER DARI TOKEN : "+userToken);
            // User hasil compare nik dari request dengan database
            Optional<KaryawanEntity> nikKOptional = karyawanRepository.findByNik(userToken);
            // Check if selectedRole is present and not empty (Ini yang dirubah)
            String selectedRole = request.getSelectedRole();
            JabatanEntity jabatan = null;
            if (selectedRole != null && !selectedRole.isEmpty()) {
                jabatan = jabatanRepository.findById(selectedRole)
                        .orElseThrow(() -> new RuntimeException("Jabatan not found for id: " + selectedRole));
            } else {
                throw new RuntimeException("Selected Role cannot be null or empty");
            }

            // Check if projectId is present in the request
            ProjectEntity project = null;
            if (request.getSelectedProject() != null && !request.getSelectedProject().isEmpty()) {
                project = projectRepository.findById(request.getSelectedProject())
                        .orElseThrow(() -> new RuntimeException("Project not found for id: " + request.getSelectedProject()));
            }

            // Check if individual fields are null or empty
            if (request.getNama() == null || request.getNama().isEmpty()) {
                throw new RuntimeException("Full Name cannot be null or empty");
            }

            if (request.getNomorKtp() == null || request.getNomorKtp().isEmpty()) {
                throw new RuntimeException("Nomor KTP cannot be null or empty");
            }

            if (request.getEmail() == null || request.getEmail().isEmpty()) {
                throw new RuntimeException("Email cannot be null or empty");
            }

            if (request.getTanggalLahir() == null) {
                throw new RuntimeException("Tanggal Lahir cannot be null");
            }

            if (request.getJenisKelamin() == null || request.getJenisKelamin().isEmpty()) {
                throw new RuntimeException("Jenis Kelamin cannot be null or empty");
            }

            if (request.getTanggalBergabung() == null) {
                throw new RuntimeException("Tanggal Bergabung cannot be null");
            }

            if (request.getHakCuti() == null) {
                throw new RuntimeException("Hak Cuti cannot be null");
            }

            if (request.getIsLeader() == null || request.getIsLeader().isEmpty()) {
                throw new RuntimeException("Is Leader cannot be null or empty");
            }
            
            if (request.getIsKaryawan() == null || request.getIsKaryawan().isEmpty()) {
                throw new RuntimeException("Is Karyawan cannot be null or empty");
            }

            KaryawanEntity nikK = nikKOptional.get();
            if (nikKOptional.isPresent()) {
                nikK.setNama(request.getNama());
                nikK.setTempatLahir(request.getTempatLahir());
                nikK.setTanggalLahir(request.getTanggalLahir());
                nikK.setJenisKelamin(request.getJenisKelamin());
                nikK.setAgama(request.getAgama());
                nikK.setKewarganegaraan(request.getKewarganegaraan());
                nikK.setKodePos(request.getKodePos());
                nikK.setAlamatKtp(request.getAlamatKtp());
                nikK.setNoHp(request.getNoHp());
                nikK.setEmail(request.getEmail());
                nikK.setNoRek(request.getNoRek());
                nikK.setNoNpwp(request.getNoNpwp());
                nikK.setJenjangPendidikan(request.getJenjangPendidikan());
                // Continue updating other fields as needed
                nikK.setTanggalBergabung(request.getTanggalBergabung());
                nikK.setAlamatSekarang(request.getAlamatSekarang());
                nikK.setStatusPerkawinan(request.getStatusPerkawinan());
                nikK.setGolonganDarah(request.getGolonganDarah());
                nikK.setNomorKtp(request.getNomorKtp());
                nikK.setEmergencyContact(request.getEmergencyContact());
                nikK.setStatusEmergency(request.getStatusEmergency());
                nikK.setAlamatEmergency(request.getAlamatEmergency());
                nikK.setTelpEmergency(request.getTelpEmergency());
                nikK.setProjectId(project);
                nikK.setDivisi(request.getDivisi());
                nikK.setNikLeader(request.getNikLeader());
                nikK.setIsLeader(request.getIsLeader());
                nikK.setUsrUpd(request.getUsrUpd());
                nikK.setHandsetImei(request.getHandsetImei());
                nikK.setHakCuti(request.getHakCuti());
                nikK.setIsKaryawan(request.getIsKaryawan());

                
            }

            Optional<KaryawanImageEntity> nikKIOptional = karyawanImageRepository.findByNik(userToken);
                KaryawanImageEntity nikKI = nikKIOptional.get();
            if(nikKIOptional.isPresent()) {
                
                nikKI.setFoto(request.getFoto() != null ? request.getFoto() : null);
                nikKI.setFotoKtp(request.getFotoKtp() != null ? request.getFotoKtp() : null);
                nikKI.setFotoNpwp(request.getFotoNpwp() != null ? request.getFotoNpwp() : null);
                nikKI.setFotoKk(request.getFotoKk() != null ? request.getFotoKk() : null);
                nikKI.setFotoAsuransi(request.getFotoAsuransi() != null ? request.getFotoAsuransi() : null);
                nikKI.setFotoPath(request.getFotoPath() != null ? request.getFotoPath() : null);
                nikKI.setFotoKtpPath(request.getFotoKtpPath() != null ? request.getFotoKtpPath() : null);
                nikKI.setFotoNpwpPath(request.getFotoNpwpPath() != null ? request.getFotoNpwpPath() : null);
                nikKI.setFotoKkPath(request.getFotoKkPath() != null ? request.getFotoKkPath() : null);
                nikKI.setFotoAsuransiPath(request.getFotoAsuransiPath() != null ? request.getFotoAsuransiPath() : null);;
                                
                
            }
            Optional<KaryawanEntity> user = karyawanRepository.findByNik(userToken);
            String nama = user.get().getNama();

            Optional<SysUserEntity> userIdOptional = sysUserRepository.findByUserId(userToken);
            long currentTimeMillis = System.currentTimeMillis();
            Timestamp dtmCrt = new Timestamp(currentTimeMillis - (currentTimeMillis % 1000));
            if(userIdOptional.isPresent()) {
                    SysUserEntity userId = userIdOptional.get();
                    userId.setFullName(request.getNama());
                    userId.setRole(jabatan);
                    userId.setUsrUpd(nama);
                    userId.setDtmUpd(dtmCrt);
                    userId.setEmail(request.getEmail());
                    System.out.println(userId.getEmail());
                    // Ambil password dari user
                    // Set Password kalau old Pw dari request sesuai dengan pw di database

                // Check if the old password is provided and matches the stored password
            if (request.getOldPassword() != null && !request.getOldPassword().isEmpty()) {
                String storedPassword = userId.getSqlPassword();

                if (passwordEncoder.matches(request.getOldPassword(), storedPassword)) {
                    // Check if the new password and confirmation password match
                    if (request.getNewPassword() != null && request.getNewPassword().equals(request.getConfPassword())) {
                        // Update the password
                        userId.setSqlPassword(passwordEncoder.encode(request.getNewPassword()));
                        userId.setIsPassChg("1");
                        userId.setLastPasschg(new Timestamp(System.currentTimeMillis()));
                    } else {
                        Map<String, String> response = new HashMap<>();
                        response.put("status", "failed");
                        response.put("message", "Sorry, the new password and confirmation password don't match. Please make sure they match to proceed.");
                        return ResponseEntity.status(HttpStatus.OK).body(response);
                    }
                } else {
                    Map<String, String> response = new HashMap<>();
                    response.put("status", "failed");
                    response.put("message", "Old password is incorrect.");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                }
            }       
                    karyawanRepository.save(nikK);
                    karyawanImageRepository.save(nikKI);
                    sysUserRepository.save(userId);

            }

            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Update Success!");

            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "failed");
            response.put("message", "Update failed!");
            response.put("error", e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    public ResponseEntity<Map<String, Object>> getUpdateProfile(
        @RequestHeader("Authorization") String jwtToken
        ) {
            Map<String, Object> response = new HashMap<>();
            try{
                if(jwtToken.startsWith("Bearer ")){
                String token = jwtToken.substring(7);
                String nik = jwtService.extractUsername(token);
                Optional<KaryawanEntity> dataKaryawan = karyawanRepository.findByNik(nik);
                response.put("success", true);
                response.put("message", "berhasil get data sebelum update profile");
                response.put("data", dataKaryawan);
                return ResponseEntity.status(HttpStatus.OK).body(response);
                }else {
                // Invalid token format
                response.put("success", false);
                response.put("message", "Invalid token format");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
                }
            }catch (Exception e){
                response.put("success", false);
                response.put("message", "error");
                response.put("error", e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        }
    

    // Helper method to convert MultipartFile to Base64
    private String convertToBase64(MultipartFile file) {
        try {
            if (file != null) {
                byte[] bytes = file.getBytes();
                return Base64.getEncoder().encodeToString(bytes);
            }
        } catch (IOException e) {
            // Handle the exception, for example, log it
            e.printStackTrace();
        }
        return null; // or an empty string if needed
    }

    public ResponseEntity<Map<String, String>> updateProfileMobile(
        ProfileRequest request, 
        @RequestHeader("Authorization") String jwtToken
        ) {
        try {
            // Cari siapa yang akses api ini
            String token = jwtToken.substring(7);
            String nik = jwtService.extractUsername(token);
            
            System.out.println("CEBELAPA IMOET CI NIK AQ: "+nik);
            // User hasil compare nik dari request dengan database
            Optional<KaryawanEntity> karyawanOptional = karyawanRepository.findByNik(nik);
            // Check if selectedRole is present and not empty (Ini yang dirubah)
    

            KaryawanEntity karyawan = karyawanOptional.get();
            if (karyawanOptional.isPresent()) {
                karyawan.setNama(request.getNama());
                karyawan.setTempatLahir(request.getTempatLahir());
                karyawan.setTanggalLahir(request.getTanggalLahir());
                karyawan.setJenisKelamin(request.getJenisKelamin());
                karyawan.setAgama(request.getAgama());
                karyawan.setKewarganegaraan(request.getKewarganegaraan());
                karyawan.setKodePos(request.getKodePos());
                karyawan.setAlamatKtp(request.getAlamatKtp());
                karyawan.setNoHp(request.getNoHp());
                karyawan.setEmail(request.getEmail());
                karyawan.setNoRek(request.getNoRek());
                karyawan.setJenjangPendidikan(request.getJenjangPendidikan());
                karyawan.setTanggalBergabung(request.getTanggalBergabung());
                karyawan.setAlamatSekarang(request.getAlamatSekarang());
                karyawan.setStatusPerkawinan(request.getStatusPerkawinan());
                karyawan.setGolonganDarah(request.getGolonganDarah());
                karyawan.setEmergencyContact(request.getEmergencyContact());
                karyawan.setStatusEmergency(request.getStatusEmergency());
                karyawan.setAlamatEmergency(request.getAlamatEmergency());
                karyawan.setTelpEmergency(request.getTelpEmergency());
                karyawan.setNomorKtp(request.getNomorKtp());
                karyawan.setNoNpwp(request.getNoNpwp());
                /*
                  nama
                  nik
                  tempatLahir
                  tanggalLahir
                  jenisKelamin
                  agama
                  kewarganegaraan
                  alamatKtp
                  kodePos
                  alamatSekarang
                  noHp
                  email
                  noRek
                  jenjangPendidikan
                  tanggalBergabung
                  statusPerkawinan
                  golonganDarah
                  emergencyContact
                  statusEmergency
                  alamatEmergency
                  telpEmergency
                  nomorKtp
                  nomorNpwp
                 */
            }
            Optional<KaryawanEntity> user = karyawanRepository.findByNik(nik);
            String nama = user.get().getNama();

            Optional<SysUserEntity> sysUserOptional = sysUserRepository.findByUserId(nik);
            long currentTimeMillis = System.currentTimeMillis();
            Timestamp dtmCrt = new Timestamp(currentTimeMillis - (currentTimeMillis % 1000));
            if(sysUserOptional.isPresent()) {
                    SysUserEntity sysUser = sysUserOptional.get();
                    sysUser.setFullName(request.getNama());
                    sysUser.setUsrUpd(nama);
                    sysUser.setDtmUpd(dtmCrt);
                    sysUser.setEmail(request.getEmail());
                    // Ambil password dari user
                    // Set Password kalau old Pw dari request sesuai dengan pw di database
                    karyawanRepository.save(karyawan);
                    sysUserRepository.save(sysUser);
            }

            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Update Success!");

            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "failed");
            response.put("message", "Update failed!");
            response.put("error", e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}