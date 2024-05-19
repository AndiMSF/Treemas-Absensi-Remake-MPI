package com.treemaswebapi.treemaswebapi.service.MasterData.Karyawan;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import com.treemaswebapi.treemaswebapi.config.JwtService;
import com.treemaswebapi.treemaswebapi.controller.Dashboard.DashboardResponse;
import com.treemaswebapi.treemaswebapi.controller.MasterData.Karyawan.request.KaryawanAddRequest;
import com.treemaswebapi.treemaswebapi.entity.CutiEntity.CutiEntity;
import com.treemaswebapi.treemaswebapi.entity.JabatanEntity.JabatanEntity;
import com.treemaswebapi.treemaswebapi.entity.KaryawanEntity.KaryawanEntity;
import com.treemaswebapi.treemaswebapi.entity.KaryawanEntity.KaryawanImageEntity;
import com.treemaswebapi.treemaswebapi.entity.ProjectEntity.ProjectEntity;
import com.treemaswebapi.treemaswebapi.entity.SysUserEntity.SysUserEntity;
import com.treemaswebapi.treemaswebapi.entity.UserRole.Role;
import com.treemaswebapi.treemaswebapi.repository.AbsenRepository;
import com.treemaswebapi.treemaswebapi.repository.CutiAppRepository;
import com.treemaswebapi.treemaswebapi.repository.CutiRepository;
import com.treemaswebapi.treemaswebapi.repository.GeneralParamRepository;
import com.treemaswebapi.treemaswebapi.repository.JabatanRepository;
import com.treemaswebapi.treemaswebapi.repository.KaryawanImageRepository;
import com.treemaswebapi.treemaswebapi.repository.KaryawanRepository;
import com.treemaswebapi.treemaswebapi.repository.ProjectRepository;
import com.treemaswebapi.treemaswebapi.repository.SysUserRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
    public class KaryawanService {
        
        private final KaryawanRepository karyawanRepository;
        private final SysUserRepository sysUserRepository;
        private final KaryawanImageRepository karyawanImageRepository;
        private final GeneralParamRepository generalParamRepository;
        private final CutiAppRepository cutiAppRepository;
        private final JabatanRepository jabatanRepository;
        private final ProjectRepository projectRepository;
        private final AbsenRepository absenRepository;

        private final PasswordEncoder passwordEncoder;
        private final JwtService jwtService;

        public ResponseEntity<Map<String, String>> karyawanAdd(
            KaryawanAddRequest request,
            @RequestHeader("Authorization") String jwtToken
        ) {
            try {            

                // Real Token terpisah dari Bearer 
                String token = jwtToken.substring(7);
                System.out.println("TOKEN : "+token);

                // Cari nik dari token
                String userToken = jwtService.extractUsername(token);
                // Cari nama dari token
                Optional<KaryawanEntity> user = karyawanRepository.findByNik(userToken);
                String nama = user.get().getNama();

                long currentTimeMillis = System.currentTimeMillis();
                Timestamp dtmCrt = new Timestamp(currentTimeMillis - (currentTimeMillis % 1000));
                // Ambil default value LEAVE di table generalparam
                String defaultCutiString = generalParamRepository.findById("LEAVE")
                    .orElseThrow(() -> new RuntimeException("LEAVE not found"))
                    .getVal();

                // Karena defaultCuti adalah String di table general param dan field sisa cuti di table cuti entity adalah BigDecimal kita konversi
                // defaultCuti yang String ke BigDecimal
                BigDecimal defaultCuti = new BigDecimal(defaultCutiString);

                // Check if selectedRole is present and not empty (Ini yang dirubah)
                String selectedRole = request.getSelectedRole();
                JabatanEntity jabatan = null;
                if (selectedRole != null && !selectedRole.isEmpty()) {
                    jabatan = jabatanRepository.findById(selectedRole)
                            .orElseThrow(() -> new RuntimeException("Jabatan not found for id: " + selectedRole));
                } else {
                    throw new RuntimeException("Selected Role cannot be null or empty");
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


                // Check if projectId is present in the request
                ProjectEntity project = null;
                if (request.getSelectedProject() != null && !request.getSelectedProject().isEmpty()) {
                    project = projectRepository.findById(request.getSelectedProject())
                            .orElseThrow(() -> new RuntimeException("Project not found for id: " + request.getSelectedProject()));
                }
                // Harus Head
                Optional<SysUserEntity> optionalSysUser = sysUserRepository.findByUserId(userToken);

                if (optionalSysUser.get().getRole() != null && optionalSysUser.get().getRole().getJabatanId().equals("HEAD")) {
                    // Mengirim ke table Karyawan
                 var karyawan = KaryawanEntity.builder()
                    .nik(request.getNik())
                    .email(request.getEmail())//gue benerin siangsiang tanggal 13.12.23
                    .nama(request.getNama())
                    .tempatLahir(request.getTempatLahir())
                    .tanggalLahir(request.getTanggalLahir())
                    .jenisKelamin(request.getJenisKelamin())
                    .agama(request.getAgama())
                    .kewarganegaraan(request.getKewarganegaraan())
                    .kodePos(request.getKodePos())
                    .alamatKtp(request.getAlamatKtp())
                    .noHp(request.getNoHp())
                    .noRek(request.getNoRek())
                    .noNpwp(request.getNoNpwp())
                    .jenjangPendidikan(request.getJenjangPendidikan())
                    .tanggalBergabung(request.getTanggalBergabung())
                    .alamatSekarang(request.getAlamatSekarang())
                    .statusPerkawinan(request.getStatusPerkawinan())
                    .golonganDarah(request.getGolonganDarah())
                    .nomorKtp(request.getNomorKtp())
                    .emergencyContact(request.getEmergencyContact())
                    .statusEmergency(request.getStatusEmergency())
                    .alamatEmergency(request.getAlamatEmergency())
                    .telpEmergency(request.getTelpEmergency())
                    .projectId(project)
                    .divisi(request.getDivisi())
                    .nikLeader(request.getNikLeader())
                    .isLeader(request.getIsLeader())
                    .usrUpd(request.getUsrUpd())
                    .handsetImei(request.getHandsetImei())
                    .hakCuti(defaultCuti)
                    .isKaryawan(request.getIsKaryawan())
                    .handsetImei(null)
                .build();
                karyawanRepository.save(karyawan);
                
                    System.out.println("MASUK DIA LEADER");
                    var sysUser = SysUserEntity.builder()
                        .userId(request.getNik())
                        .email(request.getEmail())
                        .fullName(request.getNama())
                        .role(jabatan)
                        .isLogin("0") // set ke 0 karena di table ini tidak boleh null
                        .wrongPassCount((short) 0)
                        .timesLocked(0)
                        .sqlPassword(passwordEncoder.encode("123456"))
                        .usrCrt(nama)
                        .dtmCrt(dtmCrt)
                    .build();
                    sysUserRepository.save(sysUser);
               

                // Mengirim ke table Karyawan Image
                var karyawanImage = KaryawanImageEntity.builder()
                    .nik(request.getNik())
                    .foto(request.getFoto() != null ? request.getFoto() : null)
                    .fotoKtp(request.getFotoKtp() != null ? request.getFotoKtp() : null)
                    .fotoNpwp(request.getFotoNpwp() != null ? request.getFotoNpwp() : null)
                    .fotoKk(request.getFotoKk() != null ? request.getFotoKk() : null)
                    .fotoAsuransi(request.getFotoAsuransi() != null ? request.getFotoAsuransi() : null)
                    .fotoPath(request.getFotoPath() != null ? request.getFotoPath() : null)
                    .fotoKtpPath(request.getFotoKtpPath() != null ? request.getFotoKtpPath() : null)
                    .fotoNpwpPath(request.getFotoNpwpPath() != null ? request.getFotoNpwpPath() : null)
                    .fotoKkPath(request.getFotoKkPath() != null ? request.getFotoKkPath() : null)
                    .fotoAsuransiPath(request.getFotoAsuransiPath() != null ? request.getFotoAsuransiPath() : null)
                .build();
                karyawanImageRepository.save(karyawanImage);

            Map<String, String> response = new HashMap<>();
            response.put("status", "Success");
            response.put("message", "Registration Successful");

            return ResponseEntity.status(HttpStatus.OK).body(response);
                } else {
                    Map<String, String> response = new HashMap<>();
                    response.put("status", "Failed");
                    response.put("message", "Not Allowed");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                }
                
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "Failed");
            response.put("message", "Registration Failed");
            response.put("error", e.getMessage());
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    public ResponseEntity<Map<String, Object>> karyawanGet() {
        try {
            List<KaryawanEntity> karyawan = karyawanRepository.findAll();

            Map<String, Object> response = new HashMap<>();
            response.put("status", "Success");
            response.put("message", "Retrieved");
            response.put("data", karyawan);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "Failed");
            response.put("message", "Failed to retrieve karyawan");
            response.put("error", e.getMessage());
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }   

    public ResponseEntity<Map<String, Object>> karyawanGetId(
        String id
    ) {
        try {
            Optional<KaryawanEntity> karyawan = karyawanRepository.findByNik(id);
            Optional<SysUserEntity> sysUser = sysUserRepository.findByUserId(id);
            Optional<KaryawanImageEntity> karyawanImage = karyawanImageRepository.findByNik(id);

            Map<String, Object> data = new HashMap<>();
            data.put("karyawan", karyawan);
            data.put("sysUser", sysUser);
            data.put("karyawanImage", karyawanImage);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "Success");
            response.put("message", "Retrieved");
            response.put("data", data);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "Failed");
            response.put("message", "Failed to retrieve karyawan");
            response.put("error", e.getMessage());
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }   

    public ResponseEntity<Map<String, String>> karyawanDelete(
        String id
    ) {
        try {
            // Cari Karyawan di tbl_karyawan berdasarkan ID
            Optional<KaryawanEntity> karyawanOptional = karyawanRepository.findByNik(id);
            Optional<SysUserEntity> sysUserOptional = sysUserRepository.findByUserId(id);
            Optional<KaryawanImageEntity> karyawanImageOptional = karyawanImageRepository.findByNik(id);
            if (karyawanOptional.isPresent()) {
                karyawanRepository.deleteById(id);
                sysUserRepository.deleteById(id);
                karyawanImageRepository.deleteById(id);
                Map<String, String> response = new HashMap<>();
                response.put("status", "Success");
                response.put("message", "User deleted");
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                Map<String, String> response = new HashMap<>();
                response.put("status", "Failed");
                response.put("message", "User not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "Failed");
            response.put("message", "Failed To Delete User");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    public ResponseEntity<Map<String, String>> karyawanEdit(
            String id,
            @RequestHeader("Authorization") String jwtToken,
            KaryawanAddRequest request
        ) {
            try {            
                // Real Token terpisah dari Bearer 
                String token = jwtToken.substring(7);
                System.out.println("TOKEN : "+token);

                // Cari nik dari token
                String userToken = jwtService.extractUsername(token);

                // Cari nama dari token
                Optional<KaryawanEntity> optionalNikToken = karyawanRepository.findByNik(userToken);
                // Cari data dari id
                Optional<KaryawanEntity> optionalNik = karyawanRepository.findByNik(id);
                Optional<SysUserEntity> getRoleInToken = sysUserRepository.findByUserId(userToken);
                Optional<SysUserEntity> optionalSysUser = sysUserRepository.findByUserId(id);
                Optional<KaryawanImageEntity> optionalKaryawanImage = karyawanImageRepository.findByNik(id);

                if (getRoleInToken.get().getRole() != null && getRoleInToken.get().getRole().getJabatanId().equals("HEAD")) {
                    if (optionalNik.isPresent()) {
                    String nama = optionalNikToken.get().getNama();

                long currentTimeMillis = System.currentTimeMillis();
                Timestamp dtmCrt = new Timestamp(currentTimeMillis - (currentTimeMillis % 1000));
                // Ambil default value LEAVE di table generalparam
                String defaultCutiString = generalParamRepository.findById("LEAVE")
                    .orElseThrow(() -> new RuntimeException("LEAVE not found"))
                    .getVal();

                // Karena defaultCuti adalah String di table general param dan field sisa cuti di table cuti entity adalah BigDecimal kita konversi
                // defaultCuti yang String ke BigDecimal
                BigDecimal defaultCuti = new BigDecimal(defaultCutiString);
                
                // Check if selectedRole is present and not empty (Ini yang dirubah)
                String selectedRole = request.getSelectedRole();
                JabatanEntity jabatan = null;
                if (selectedRole != null && !selectedRole.isEmpty()) {
                    jabatan = jabatanRepository.findById(selectedRole)
                            .orElseThrow(() -> new RuntimeException("Jabatan not found for id: " + selectedRole));
                } else {
                    throw new RuntimeException("Selected Role cannot be null or empty");
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

                // Check if projectId is present in the request
                ProjectEntity project = null;
                if (request.getSelectedProject() != null && !request.getSelectedProject().isEmpty()) {
                    project = projectRepository.findById(request.getSelectedProject())
                            .orElseThrow(() -> new RuntimeException("Project not found for id: " + request.getSelectedProject()));
                }

                    KaryawanEntity karyawan = optionalNik.get();
                    karyawan.setNama(request.getNama());
                    karyawan.setEmail(request.getEmail());
                    karyawan.setTempatLahir(request.getTempatLahir());
                    karyawan.setTanggalLahir(request.getTanggalLahir());
                    karyawan.setJenisKelamin(request.getJenisKelamin());
                    karyawan.setAgama(request.getAgama());
                    karyawan.setKewarganegaraan(request.getKewarganegaraan());
                    karyawan.setKodePos(request.getKodePos());
                    karyawan.setAlamatKtp(request.getAlamatKtp());
                    karyawan.setNoHp(request.getNoHp());
                    karyawan.setNoRek(request.getNoRek());
                    karyawan.setNoNpwp(request.getNoNpwp());
                    karyawan.setJenjangPendidikan(request.getJenjangPendidikan());
                    karyawan.setTanggalBergabung(request.getTanggalBergabung());
                    karyawan.setAlamatSekarang(request.getAlamatSekarang());
                    karyawan.setStatusPerkawinan(request.getStatusPerkawinan());
                    karyawan.setGolonganDarah(request.getGolonganDarah());
                    karyawan.setNomorKtp(request.getNomorKtp());
                    karyawan.setEmergencyContact(request.getEmergencyContact());
                    karyawan.setStatusEmergency(request.getStatusEmergency());
                    karyawan.setAlamatEmergency(request.getAlamatEmergency());
                    karyawan.setTelpEmergency(request.getTelpEmergency());
                    karyawan.setProjectId(project);
                    karyawan.setDivisi(request.getDivisi());
                    karyawan.setNikLeader(request.getNikLeader());
                    karyawan.setIsLeader(request.getIsLeader());
                    karyawan.setUsrUpd(request.getUsrUpd());
                    karyawan.setHandsetImei(request.getHandsetImei());
                    karyawan.setHakCuti(defaultCuti);
                    karyawan.setIsKaryawan(request.getIsKaryawan());

                    karyawanRepository.save(karyawan);
                
                // Mengirim ke table Sys_User
                    SysUserEntity sysUser = optionalSysUser.get();
                    sysUser.setUserId(request.getNik());
                    sysUser.setEmail(request.getEmail());
                    sysUser.setFullName(request.getNama());
                    sysUser.setRole(jabatan);
                    sysUser.setIsLogin(sysUser.getIsLogin());
                    sysUser.setWrongPassCount(sysUser.getWrongPassCount());
                    sysUser.setTimesLocked(sysUser.getTimesLocked());
                    sysUser.setSqlPassword(sysUser.getSqlPassword());
                    sysUser.setUsrCrt(nama);
                    if (sysUser.getDtmCrt() != null) {
                        sysUser.setDtmCrt(dtmCrt);
                    }
                    sysUser.setDtmUpd(dtmCrt);
                    sysUser.setUsrUpd(nama);

                    sysUserRepository.save(sysUser);
                

                // Mengirim ke table Karyawan Image
                    KaryawanImageEntity karyawanImage = optionalKaryawanImage.get();
                    karyawanImage.setFoto(request.getFoto() != null ? request.getFoto() : null);
                    karyawanImage.setFotoKtp(request.getFotoKtp() != null ? request.getFotoKtp() : null);
                    karyawanImage.setFotoNpwp(request.getFotoNpwp() != null ? request.getFotoNpwp() : null);
                    karyawanImage.setFotoKk(request.getFotoKk() != null ? request.getFotoKk() : null);
                    karyawanImage.setFotoAsuransi(request.getFotoAsuransi() != null ? request.getFotoAsuransi() : null);
                    karyawanImage.setFotoPath(request.getFotoPath() != null ? request.getFotoPath() : null);
                    karyawanImage.setFotoKtpPath(request.getFotoKtpPath() != null ? request.getFotoKtpPath() : null);
                    karyawanImage.setFotoNpwpPath(request.getFotoNpwpPath() != null ? request.getFotoNpwpPath() : null);
                    karyawanImage.setFotoKkPath(request.getFotoKkPath() != null ? request.getFotoKkPath() : null);
                    karyawanImage.setFotoAsuransiPath(request.getFotoAsuransiPath() != null ? request.getFotoAsuransiPath() : null);

                    karyawanImageRepository.save(karyawanImage);

            Map<String, String> response = new HashMap<>();
            response.put("status", "Success");
            response.put("message", "Karyawan Edit Successful");

            return ResponseEntity.status(HttpStatus.OK).body(response);
                } else {
                    Map<String, String> response = new HashMap<>();
                    response.put("status", "Failed");
                    response.put("message", "User not found");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                }
            } else {
                Map<String, String> response = new HashMap<>();
                    response.put("status", "Failed");
                    response.put("message", "Not Allowed");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

                
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "Failed");
            response.put("message", "Karyawan Edit Failed");
            response.put("error", e.getMessage());
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    public ResponseEntity<Map<String, Object>> getDataPribadi(@RequestHeader("Authorization") String tokenWithBearer,DashboardResponse dashboardResponse) {
        
        try{
            Map<String, Object> response = new HashMap<>();
            if (tokenWithBearer.startsWith("Bearer ")) {
            String token = tokenWithBearer.substring("Bearer ".length());
            String nik = jwtService.extractUsername(token);
            int masuk = absenRepository.countByJamMskIsNotNullAndNik(nik);
            int totalMasuk = masuk;
            int totalSakit = cutiAppRepository.countByIsApprovedAndNikAndFlgKet("1", nik, "sakit");
            int totalTelatMasuk = absenRepository.countByNoteTelatMskIsNotNullAndNik(nik);
            int totalPulangCepat = absenRepository.countByNotePlgCepatIsNotNullAndNik(nik);
            int totalCuti = cutiAppRepository.countByIsApprovedAndNikAndFlgKet("1", nik, "cuti");
            int totalTidakMasuk = absenRepository.countByJamMskIsNullAndJamPlgIsNullAndNik(nik);
            dashboardResponse.setTotalMasuk(totalMasuk);
            dashboardResponse.setTotalSakit(totalSakit);
            dashboardResponse.setTotalTelatMasuk(totalTelatMasuk);
            dashboardResponse.setTotalPulangCepat(totalPulangCepat);
            dashboardResponse.setTotalCuti(totalCuti);
            dashboardResponse.setTotalTidakMasuk(totalTidakMasuk);

            response.put("success", true);
            response.put("message", "berhasil get data kehadiran karyawan");
            response.put("data", dashboardResponse);

            return ResponseEntity.status(HttpStatus.OK).body(response);
            }else{
            response.put("success", false);
            response.put("message", "Invalid Token");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }
        }catch (Exception e){
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
public static Role convertToRole(String roleName) {
    for (Role role : Role.values()) {
        if (role.name().equalsIgnoreCase(roleName)) {
            return role;
        }
    }
    throw new IllegalArgumentException("Invalid role name: " + roleName);
}
}

