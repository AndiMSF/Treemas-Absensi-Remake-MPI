package com.treemaswebapi.treemaswebapi.service.AbsenService;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.treemaswebapi.treemaswebapi.config.JwtService;
import com.treemaswebapi.treemaswebapi.controller.AbsenController.request.AbsenRequest;
import com.treemaswebapi.treemaswebapi.entity.ProjectEntity.ProjectEntity;
import com.treemaswebapi.treemaswebapi.entity.ReimburseEntity.ReimburseAppEntity;
import com.treemaswebapi.treemaswebapi.entity.SysUserEntity.SysUserEntity;
import com.treemaswebapi.treemaswebapi.entity.TimesheetEntity.TimesheetEntity;
import com.treemaswebapi.treemaswebapi.entity.AbsenEntity.AbsenEntity;
import com.treemaswebapi.treemaswebapi.entity.AbsenEntity.AbsenImgEntity;
import com.treemaswebapi.treemaswebapi.entity.AbsenEntity.AbsenPulangAppEntity;
import com.treemaswebapi.treemaswebapi.controller.AbsenController.AbsenBelumPulangResponse;
import com.treemaswebapi.treemaswebapi.entity.AbsenEntity.AbsenTrackingEntity;
import com.treemaswebapi.treemaswebapi.entity.CutiEntity.CutiEntity;
import com.treemaswebapi.treemaswebapi.entity.PenempatanEntity.PenempatanEntity;
import com.treemaswebapi.treemaswebapi.entity.ProjectEntity.ProjectDetails;
import com.treemaswebapi.treemaswebapi.repository.AbsenImgRepository;
import com.treemaswebapi.treemaswebapi.repository.AbsenPulangAppRepository;
import com.treemaswebapi.treemaswebapi.repository.AbsenRepository;
import com.treemaswebapi.treemaswebapi.repository.AbsenTrackingRepository;
import com.treemaswebapi.treemaswebapi.repository.CutiRepository;
import com.treemaswebapi.treemaswebapi.repository.KaryawanRepository;
import com.treemaswebapi.treemaswebapi.repository.PenempatanRepository;
import com.treemaswebapi.treemaswebapi.repository.ProjectRepository;
import com.treemaswebapi.treemaswebapi.repository.ReimburseAppRepository;
import com.treemaswebapi.treemaswebapi.repository.ReimburseRepository;
import com.treemaswebapi.treemaswebapi.repository.SysUserRepository;
import com.treemaswebapi.treemaswebapi.repository.TimesheetRepository;

import jakarta.persistence.Temporal;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AbsenService {

    private final JwtService jwtService;
    private final PenempatanRepository penempatanRepository;
    private final ProjectRepository projectRepository;
    private final AbsenRepository absenRepository;
    private final AbsenTrackingRepository absenTrackingRepository;
    private final AbsenImgRepository absenImgRepository;
    private final KaryawanRepository karyawanRepository;
    private final AbsenPulangAppRepository absenPulangAppRepository;
    private final TimesheetRepository timesheetRepository;
    private final ReimburseAppRepository reimburseAppRepository;
    private final CutiRepository cutiRepository;
    private final SysUserRepository sysUserRepository;

    private static String getIndonesianDayOfWeek(DayOfWeek dayOfWeek){
        Map<String,String> indonesianDayMap = new HashMap<>();
        indonesianDayMap.put("MONDAY", "Senin");
        indonesianDayMap.put("TUESDAY", "Selasa");
        indonesianDayMap.put("WEDNESDAY", "Rabu");
        indonesianDayMap.put("THURSDAY", "Kamis");
        indonesianDayMap.put("FRIDAY", "Jumat");
        indonesianDayMap.put("SATURDAY", "Sabtu");
        indonesianDayMap.put("SUNDAY", "Minggu");

        return indonesianDayMap.get(dayOfWeek.toString());
    }

    public ResponseEntity<Map<String, Object>> getProjectDetails(String tokenWithBearer) {
        List<ProjectDetails> projectDetails = new ArrayList<>();
    
        try {
            if (tokenWithBearer.startsWith("Bearer ")) {
                String token = tokenWithBearer.substring("Bearer ".length());
                String nik = jwtService.extractUsername(token);
    
                List<PenempatanEntity> penempatanEntities = penempatanRepository.findByNik(nik);
    
                for (PenempatanEntity penempatanEntity : penempatanEntities) {
                    String projectId = penempatanEntity.getProjectId().toString();
                    ProjectEntity project = projectRepository.findByProjectId(projectId);
                    if (project != null) {
                        ProjectDetails projectDetail = new ProjectDetails();
                        projectDetail.setProjectId(projectId.toString());
                        projectDetail.setProjectName(project.getNamaProject());
                        projectDetail.setProjectAddress(project.getLokasi());
                        projectDetails.add(projectDetail);
                    }
                }
    
                Map<String, Object> response = new HashMap<>();
                if (!projectDetails.isEmpty()) {
                    response.put("success", true);
                    response.put("message", "Project details retrieved successfully");
                    response.put("data", projectDetails);
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                } else {
                    response.put("success", false);
                    response.put("message", "No projects found for the user");
                    response.put("data", null);
                    response.put("nik", nik);
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                }
            } else {
                // Handle the case where "Bearer " is not present
                System.out.println("Invalid token format");
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to retrieve project details");
            response.put("error", e.getMessage());
    
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    
        // In case of any exception or invalid token format, return an appropriate response
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", "Failed to retrieve project details");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
    
    
    public ResponseEntity<Map<String, Object>> inputAbsen(@RequestHeader("Authorization") String tokenWithBearer, AbsenRequest request) {
        try {
            
            if (tokenWithBearer.startsWith("Bearer ")) {
                String token = tokenWithBearer.substring("Bearer ".length());
                String nik = jwtService.extractUsername(token);
                System.out.println(nik);
                String nama = karyawanRepository.findNamaByNik(nik);
                if (nik != null) {
                    LocalDate tanggalIni = LocalDate.parse(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    Timestamp currentTimestamp = Timestamp.valueOf(LocalDateTime.now());
                    if("1".equals(request.getIsOther())){
                        LocalTime jamSekarang = LocalTime.parse(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                        AbsenEntity absenEntity = absenRepository.findByTglAbsenAndNik(tanggalIni, nik);
                        absenEntity.setNik(nik);
                        absenEntity.setJamMsk(jamSekarang);
                        absenEntity.setNama(nama);
                        absenEntity.setTglAbsen(tanggalIni);
                        absenEntity.setNoteOther(request.getNoteOther());
                        absenEntity.setUsrCrt(nama); 
                        absenEntity.setDtmCrt(currentTimestamp);
                        absenEntity.setIsOther(request.getIsOther());
                        absenRepository.save(absenEntity);

                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("message", "Special absen data inserted successfully");
                    response.put("data", absenEntity);

                    return ResponseEntity.status(HttpStatus.OK).body(response);
                    }
                    System.out.println(nama);
                    if (nama.isEmpty()) {
                        // NIK not found in KaryawanEntity
                        Map<String, Object> response = new HashMap<>();
                        response.put("success", false);
                        response.put("message", "NIK not found in KaryawanEntity. Please contact the administrator.");
                        response.put("data", "niknya gaada nih si: "+nik);
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                    }
                    List<PenempatanEntity> registeredProjects = penempatanRepository.findAllByNik(nik);
                    // ini buat ngecek penempatan entitinya, ada apa engga dia
                    boolean projectFound = false;
                    String projectRequest = request.getProjectId().getProjectId().toString();
                    for (PenempatanEntity projReq : registeredProjects) {
                        if (projReq.getProjectId().getProjectId().equals(projectRequest)) {
                            projectFound = true;
                            // break;
                        }
                    }
                    System.out.println(projectFound);
                    if (projectFound) {
                        ProjectEntity project = projectRepository.findByProjectId(projectRequest);
                        if (project != null) {
                            // Save to absen Entity
                            LocalTime jamSekarang = LocalTime.parse(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                            AbsenEntity absenEntity = new AbsenEntity();
                            absenEntity.setProjectId(project);
                            absenEntity.setNik(nik);
                            absenEntity.setNama(nama);
                            absenEntity.setDtmCrt(Timestamp.valueOf(LocalDateTime.now()));
                            absenEntity.setHari(getIndonesianDayOfWeek(LocalDate.now().getDayOfWeek()));
                            absenEntity.setLokasiMsk(request.getLokasiMsk());
                            absenEntity.setJamMsk(jamSekarang);
                            absenEntity.setJarakMsk(request.getJarakMsk());
                            absenEntity.setTglAbsen(tanggalIni);
                            absenEntity.setNoteTelatMsk(request.getNoteTelatMsk());
                            absenEntity.setGpsLatitudeMsk(request.getGpsLatitudeMsk());
                            absenEntity.setGpsLongitudeMsk(request.getGpsLongitudeMsk());
                            absenEntity.setIsWfh(request.getIsWfh());
                            absenRepository.save(absenEntity);
                            System.out.println("sekarang tuh jam segini +" + jamSekarang);
                            // Save to absentrackingentity
                            AbsenTrackingEntity absenTrackingEntity = new AbsenTrackingEntity();
                            absenTrackingEntity.setId(absenEntity.getId());
                            absenTrackingEntity.setProjectId(project);
                            absenTrackingEntity.setNik(nik);
                            absenTrackingEntity.setNama(nama);
                            absenTrackingEntity.setHari(getIndonesianDayOfWeek(LocalDate.now().getDayOfWeek()));
                            absenTrackingEntity.setLokasiMsk(request.getLokasiMsk());
                            absenTrackingEntity.setJamMsk(jamSekarang);
                            absenTrackingEntity.setJarakMsk(request.getJarakMsk());
                            absenTrackingEntity.setTglAbsen(tanggalIni);
                            absenTrackingEntity.setNoteTelatMsk(request.getNoteTelatMsk());
                            absenTrackingEntity.setGpsLatitudeMsk(request.getGpsLatitudeMsk());
                            absenTrackingEntity.setGpsLongitudeMsk(request.getGpsLongitudeMsk());
                            absenTrackingEntity.setIsWfh(request.getIsWfh());

                            absenTrackingRepository.save(absenTrackingEntity);

                            Long idAbsen = absenEntity.getId();
                            AbsenImgEntity absenImgEntity = new AbsenImgEntity();
                            absenImgEntity.setNik(nik);
                            absenImgEntity.setId(idAbsen);
                            absenImgEntity.setTglAbsen(LocalDate.parse(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
                            absenImgEntity.setImage64(request.getPhotoAbsen()!= null ? request.getPhotoAbsen() : null);
                            absenImgEntity.setImage(request.getImage()!= null ? request.getImage() : null);
                            absenImgEntity.setUsrUpd(nama);
                            absenImgEntity.setDtmUpd(Timestamp.valueOf(LocalDateTime.now()));
    
                            absenImgEntity = absenImgRepository.save(absenImgEntity);
                            
                            Map<String, Object> response = new HashMap<>();
                            response.put("success", true);
                            response.put("message", "Absen data inserted successfully");
                            response.put("data", absenEntity);
    
                            return ResponseEntity.status(HttpStatus.OK).body(response);
                        } else {
                            // Project not found
                            Map<String, Object> response = new HashMap<>();
                            response.put("success", false);
                            response.put("message", "Project not found");
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                        }
                    } else {
                        // User is not authorized to access this project
                        Map<String, Object> response = new HashMap<>();
                        response.put("success", false);
                        response.put("message", "You are not authorized to access this project");
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
                    }
                }
            } else {
                // Invalid token format
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Invalid token format");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            
        } catch (Exception e) {
            // Error occurred
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to insert absen data");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    
        // Default return statement for unexpected cases
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", "Unexpected error occurred");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
   
        public ResponseEntity<Map<String, Object>> inputAbsenPulang(@RequestHeader("Authorization") String tokenWithBearer, AbsenRequest request) {
        try {
                if (tokenWithBearer.startsWith("Bearer ")){
                String token = tokenWithBearer.substring("Bearer ".length());
                String nik = jwtService.extractUsername(token);
                String nama = karyawanRepository.findNamaByNik(nik);
                // Check if the user has already done the input for the current date
                LocalDate currentDate = LocalDate.parse(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                List<AbsenEntity> existingAbsenRecords = absenRepository.findByNikAndTglAbsen(nik, currentDate);

                if (!existingAbsenRecords.isEmpty()) {
                    // User has already done the input-absen for the day
                    LocalTime jamSekarang = LocalTime.parse(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                    LocalDateTime sekarangBanget = LocalDateTime.now();
                    Timestamp jamIni = Timestamp.valueOf(sekarangBanget);
                    AbsenEntity existingAbsenEntity = existingAbsenRecords.get(0);
                    // Make separate response
                    // save ke absenEntity
                    existingAbsenEntity.setNotePekerjaan(request.getNotePekerjaan());
                    existingAbsenEntity.setGpsLatitudePlg(request.getGpsLatitudePlg());
                    existingAbsenEntity.setGpsLongitudePlg(request.getGpsLongitudePlg());
                    existingAbsenEntity.setLokasiPlg(request.getLokasiPlg());
                    existingAbsenEntity.setJarakPlg(request.getJarakPlg());
                    existingAbsenEntity.setJamPlg(jamSekarang);
                    existingAbsenEntity.setNotePlgCepat(request.getNotePlgCepat());
                    LocalTime jamMsk = existingAbsenEntity.getJamMsk();
                    LocalTime jamPlg = existingAbsenEntity.getJamPlg();
                    if (jamMsk != null && jamPlg != null) {
                        BigDecimal hoursPart = new BigDecimal(Duration.between(jamMsk, jamPlg).toHoursPart());
                        existingAbsenEntity.setTotalJamKerja(hoursPart);
                        if (hoursPart.compareTo(new BigDecimal(9))>0) {
                            existingAbsenEntity.setIsAbsen("1");
                            existingAbsenEntity.setIsLembur("1");
                        } else {
                            existingAbsenEntity.setIsAbsen("1");
                            existingAbsenEntity.setIsLembur("0");
                        }
                    }
                    
                    if (request.getNoteOther() != null) {
                        existingAbsenEntity.setNoteOther(request.getNoteOther());
                        existingAbsenEntity.setIsOther("1");
                    }
                    
                    absenRepository.save(existingAbsenEntity);
                    // save ke absenTrackingEntity
                    AbsenTrackingEntity absenTrackingEntity = new AbsenTrackingEntity();
                    
                    absenTrackingEntity.setProjectId(request.getProjectId());
                    absenTrackingEntity.setNik(nik);
                    absenTrackingEntity.setNama(nama);
                    absenTrackingEntity.setHari(getIndonesianDayOfWeek(LocalDate.now().getDayOfWeek()));
                    absenTrackingEntity.setTglAbsen(LocalDate.parse(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
                    absenTrackingEntity.setGpsLatitudePlg(request.getGpsLatitudePlg());
                    absenTrackingEntity.setGpsLongitudePlg(request.getGpsLongitudePlg());
                    absenTrackingEntity.setLokasiPlg(request.getLokasiPlg());
                    absenTrackingEntity.setJarakPlg(request.getJarakPlg());
                    absenTrackingEntity.setNotePekerjaan(request.getNotePekerjaan());
                    absenTrackingEntity.setGpsLatitudePlg(request.getGpsLatitudePlg());
                    absenTrackingEntity.setGpsLongitudePlg(request.getGpsLongitudePlg());
                    absenTrackingEntity.setLokasiPlg(request.getLokasiPlg());
                    absenTrackingEntity.setJarakPlg(request.getJarakPlg());
                    absenTrackingEntity.setJamPlg(jamSekarang);
                    absenTrackingEntity.setNotePlgCepat(request.getNotePlgCepat());
                    if (request.getNoteOther() != null) {
                        absenTrackingEntity.setNoteOther(request.getNoteOther());
                        absenTrackingEntity.setIsOther("1");
                    }

                    absenTrackingRepository.save(absenTrackingEntity);

                    TimesheetEntity timesheetEntity = new TimesheetEntity();

                    timesheetEntity.setDtmCrt(jamIni);
                    String flgKetValue = "-"; // Default value

                        if ("1".equals(existingAbsenEntity.getIsSakit())) {
                            flgKetValue = "sakit";
                        } else if ("1".equals(existingAbsenEntity.getIsCuti())) {
                            flgKetValue = "cuti";
                        }

                    timesheetEntity.setFlgKet(flgKetValue);
                    timesheetEntity.setHari(existingAbsenEntity.getHari());
                    timesheetEntity.setJamKeluar(jamSekarang);
                    timesheetEntity.setJamMasuk(jamMsk);
                    timesheetEntity.setNama(nama);
                    timesheetEntity.setNik(nik);
                    timesheetEntity.setNote(request.getNotePekerjaan());
                    timesheetEntity.setProjectId(request.getProjectId());
                    timesheetEntity.setTglMsk(existingAbsenEntity.getTglAbsen());
                    timesheetEntity.setUsrCrt(nama);

                    LocalTime jamMashook = jamMsk;
                    LocalTime jamPoelang = jamSekarang;
                    
                    BigDecimal totalHours = BigDecimal.ZERO;
                    BigDecimal jamLembur = BigDecimal.ZERO;

                    if (jamMashook != null && jamPoelang != null){
                    Duration duration = Duration.between(jamMashook, jamPoelang);
                    long totalHoursLong = duration.toHours();
                    totalHours = BigDecimal.valueOf(totalHoursLong);
                        if (totalHoursLong > 9){
                            jamLembur = BigDecimal.valueOf(totalHoursLong-9);
                        }
                    }

                    timesheetEntity.setOvertime(jamLembur);
                    timesheetEntity.setTotalJamKerja(totalHours);
                    timesheetEntity.setProjectId(existingAbsenEntity.getProjectId());
                    timesheetEntity.setDtmCrt(jamIni);
                    timesheetRepository.save(timesheetEntity);

                    // ReimburseAppEntity reimburseApp = new ReimburseAppEntity();
                    // reimburseApp.setIsAbsen(existingAbsenEntity.getIsAbsen());
                    // reimburseApp.setNik(nik);
                    // reimburseApp.setNotePekerjaan(existingAbsenEntity.getNotePekerjaan());
                    // reimburseApp.setIsLembur(existingAbsenEntity.getIsLembur());
                    // reimburseApp.setIsLibur(existingAbsenEntity.getIsLibur());
                    // reimburseApp.setIsSakit(existingAbsenEntity.getIsSakit());
                    // reimburseApp.setDtmUpd(existingAbsenEntity.getDtmCrt());
                    // reimburseApp.setGpsLatitudeMsk(existingAbsenEntity.getGpsLatitudeMsk());
                    // reimburseApp.setGpsLongitudePlg(existingAbsenEntity.getGpsLongitudePlg());
                    // reimburseApp.setGpsLongitudeMsk(existingAbsenEntity.getGpsLongitudeMsk());
                    // reimburseApp.setGpsLatitudePlg(existingAbsenEntity.getGpsLatitudePlg());
                    // reimburseApp.setIsOther(existingAbsenEntity.getIsOther());
                    // reimburseApp.setIsWfh(existingAbsenEntity.getIsWfh());
                    // reimburseApp.setJamMsk(existingAbsenEntity.getJamMsk());
                    // reimburseApp.setJamPlg(existingAbsenEntity.getJamPlg());
                    // reimburseApp.setJarakMsk(existingAbsenEntity.getJarakMsk());
                    // String keterangan = null;
                    // if (existingAbsenEntity.getIsSakit() != null) {
                    //     keterangan = "sakit";
                    // }else if (existingAbsenEntity.getIsCuti() != null) {
                    //     keterangan = "cuti";                     
                    // }
                    // reimburseApp.setKeterangan(keterangan);
                    // reimburseApp.setLokasiMsk(existingAbsenEntity.getLokasiMsk());
                    // reimburseApp.setLokasiPlg(existingAbsenEntity.getLokasiPlg());
                    // reimburseApp.setNama(nama);
                    // reimburseApp.setNoteOther(existingAbsenEntity.getNoteOther());
                    // reimburseApp.setNotePekerjaan(existingAbsenEntity.getNotePekerjaan());
                    // reimburseApp.setNotePlgCepat(existingAbsenEntity.getNotePlgCepat());
                    // reimburseApp.setNoteTelatMsk(existingAbsenEntity.getNoteTelatMsk());
                    // reimburseApp.setProjectId(existingAbsenEntity.getProjectId());
                    // reimburseApp.setTglAbsen(existingAbsenEntity.getTglAbsen());
                    // reimburseApp.setTotalJamKerja(totalHours);
                    // reimburseApp.setUsrUpd(nama);
                    // reimburseAppRepository.save(reimburseApp);

                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("message", "Absen data inserted successfully + timesheet + reimburse");
                    response.put("data", existingAbsenEntity);
    
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                }else{
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", false);
                    response.put("message", "You have already submitted absen data for today");
                    response.put("data", existingAbsenRecords.get(0));
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                }
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Invalid token format");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to insert absen data");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // public ResponseEntity<Map<String, Object>> cancelPulang(@RequestHeader("Authorization") String tokenWithBearer) {
    //     try {
    //             if (tokenWithBearer.startsWith("Bearer ")){
    //             String token = tokenWithBearer.substring("Bearer ".length());
    //             String nik = jwtService.extractUsername(token);
    //             String nama = karyawanRepository.findNamaByNik(nik);
    //             // Check if the user has already done the input for the current date
    //             LocalDate currentDate = LocalDate.parse(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    //             List<AbsenEntity> existingAbsenRecords = absenRepository.findByNikAndTglAbsen(nik, currentDate);

    //             if (!existingAbsenRecords.isEmpty()) {
    //                 // User has already done the input-absen for the day
    //                 LocalTime jamSekarang = LocalTime.parse(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
    //                 LocalDateTime sekarangBanget = LocalDateTime.now();
    //                 Timestamp jamIni = Timestamp.valueOf(sekarangBanget);
    //                 AbsenEntity existingAbsenEntity = existingAbsenRecords.get(0);
    //                 // Make separate response
    //                 // save ke absenEntity
    //                 existingAbsenEntity.setNotePekerjaan(null);
    //                 existingAbsenEntity.setGpsLatitudePlg(null);
    //                 existingAbsenEntity.setGpsLongitudePlg(null);
    //                 existingAbsenEntity.setLokasiPlg(null);
    //                 existingAbsenEntity.setJarakPlg(null);
    //                 existingAbsenEntity.setJamPlg(null);
    //                 existingAbsenEntity.setNotePlgCepat(null);
    //                 LocalTime jamMsk = existingAbsenEntity.getJamMsk();
    //                 LocalTime jamPlg = existingAbsenEntity.getJamPlg();
    //                 if (jamMsk != null && jamPlg != null) {
    //                     BigDecimal hoursPart = new BigDecimal(Duration.between(jamMsk, jamPlg).toHoursPart());
    //                     existingAbsenEntity.setTotalJamKerja(hoursPart);
    //                     if (hoursPart.compareTo(new BigDecimal(9))>0) {
    //                         existingAbsenEntity.setIsAbsen("1");
    //                         existingAbsenEntity.setIsLembur("1");
    //                     } else {
    //                         existingAbsenEntity.setIsAbsen("1");
    //                         existingAbsenEntity.setIsLembur("0");
    //                     }
    //                 }
                    
    //                 if (existingAbsenEntity.getIsOther() != null) {
    //                     existingAbsenEntity.setNoteOther(null);
    //                     existingAbsenEntity.setIsOther("1");
    //                 }
                    
    //                 absenRepository.save(existingAbsenEntity);
    //                 // save ke absenTrackingEntity
    //                 AbsenTrackingEntity absenTrackingEntity = absenTrackingRepository.findByTglAbsenAndNik(currentDate, nik).get(0);
                    
    //                 absenTrackingEntity.setProjectId(request.getProjectId());
    //                 absenTrackingEntity.setNik(nik);
    //                 absenTrackingEntity.setNama(nama);
    //                 absenTrackingEntity.setHari(getIndonesianDayOfWeek(LocalDate.now().getDayOfWeek()));
    //                 absenTrackingEntity.setTglAbsen(LocalDate.parse(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
    //                 absenTrackingEntity.setGpsLatitudePlg(request.getGpsLatitudePlg());
    //                 absenTrackingEntity.setGpsLongitudePlg(request.getGpsLongitudePlg());
    //                 absenTrackingEntity.setLokasiPlg(request.getLokasiPlg());
    //                 absenTrackingEntity.setJarakPlg(request.getJarakPlg());
    //                 absenTrackingEntity.setNotePekerjaan(request.getNotePekerjaan());
    //                 absenTrackingEntity.setGpsLatitudePlg(request.getGpsLatitudePlg());
    //                 absenTrackingEntity.setGpsLongitudePlg(request.getGpsLongitudePlg());
    //                 absenTrackingEntity.setLokasiPlg(request.getLokasiPlg());
    //                 absenTrackingEntity.setJarakPlg(request.getJarakPlg());
    //                 absenTrackingEntity.setJamPlg(jamSekarang);
    //                 absenTrackingEntity.setNotePlgCepat(request.getNotePlgCepat());
    //                 if (request.getNoteOther() != null) {
    //                     absenTrackingEntity.setNoteOther(request.getNoteOther());
    //                     absenTrackingEntity.setIsOther("1");
    //                 }

    //                 absenTrackingRepository.save(absenTrackingEntity);

    //                 TimesheetEntity timesheetEntity = timesheetRepository.findByNikAndTglMsk(nik, currentDate);

    //                 timesheetEntity.setDtmCrt(jamIni);
    //                 String flgKetValue = "-"; // Default value

    //                     if ("1".equals(existingAbsenEntity.getIsSakit())) {
    //                         flgKetValue = "sakit";
    //                     } else if ("1".equals(existingAbsenEntity.getIsCuti())) {
    //                         flgKetValue = "cuti";
    //                     }

    //                 timesheetEntity.setFlgKet(flgKetValue);
    //                 timesheetEntity.setHari(existingAbsenEntity.getHari());
    //                 timesheetEntity.setJamKeluar(jamSekarang);
    //                 timesheetEntity.setJamMasuk(jamMsk);
    //                 timesheetEntity.setNama(nama);
    //                 timesheetEntity.setNik(nik);
    //                 timesheetEntity.setNote(request.getNotePekerjaan());
    //                 timesheetEntity.setProjectId(request.getProjectId());
    //                 timesheetEntity.setTglMsk(existingAbsenEntity.getTglAbsen());
    //                 timesheetEntity.setUsrCrt(nama);

    //                 LocalTime jamMashook = request.getJamMsk();
    //                 LocalTime jamPoelang = request.getJamPlg();
                    
    //                 BigDecimal totalHours = BigDecimal.ZERO;
    //                 BigDecimal jamLembur = BigDecimal.ZERO;

    //                 if (jamMashook != null && jamPoelang != null){
    //                 Duration duration = Duration.between(jamMashook, jamPoelang);
    //                 long totalHoursLong = duration.toHours();
    //                 totalHours = BigDecimal.valueOf(totalHoursLong);
    //                     if (totalHoursLong > 9){
    //                         jamLembur = BigDecimal.valueOf(totalHoursLong-9);
    //                     }
    //                 }

    //                 timesheetEntity.setOvertime(jamLembur);
    //                 timesheetEntity.setTotalJamKerja(totalHours);
    //                 timesheetEntity.setProjectId(existingAbsenEntity.getProjectId());
    //                 timesheetEntity.setDtmCrt(jamIni);
    //                 timesheetRepository.save(timesheetEntity);

    //                 ReimburseAppEntity reimburseApp = reimburseAppRepository.findByNikAndTglAbsen(nik, currentDate);
    //                 reimburseApp.setIsAbsen(existingAbsenEntity.getIsAbsen());
    //                 reimburseApp.setNik(nik);
    //                 reimburseApp.setNotePekerjaan(existingAbsenEntity.getNotePekerjaan());
    //                 reimburseApp.setIsLembur(existingAbsenEntity.getIsLembur());
    //                 reimburseApp.setIsLibur(existingAbsenEntity.getIsLibur());
    //                 reimburseApp.setIsSakit(existingAbsenEntity.getIsSakit());
    //                 reimburseApp.setDtmUpd(existingAbsenEntity.getDtmCrt());
    //                 reimburseApp.setGpsLatitudeMsk(existingAbsenEntity.getGpsLatitudeMsk());
    //                 reimburseApp.setGpsLongitudePlg(existingAbsenEntity.getGpsLongitudePlg());
    //                 reimburseApp.setGpsLongitudeMsk(existingAbsenEntity.getGpsLongitudeMsk());
    //                 reimburseApp.setGpsLatitudePlg(existingAbsenEntity.getGpsLatitudePlg());
    //                 reimburseApp.setIsOther(existingAbsenEntity.getIsOther());
    //                 reimburseApp.setIsWfh(existingAbsenEntity.getIsWfh());
    //                 reimburseApp.setJamMsk(existingAbsenEntity.getJamMsk());
    //                 reimburseApp.setJamPlg(existingAbsenEntity.getJamPlg());
    //                 reimburseApp.setJarakMsk(existingAbsenEntity.getJarakMsk());
    //                 reimburseApp.setLokasiMsk(existingAbsenEntity.getLokasiMsk());
    //                 reimburseApp.setLokasiPlg(existingAbsenEntity.getLokasiPlg());
    //                 reimburseApp.setNama(nama);
    //                 reimburseApp.setNoteOther(existingAbsenEntity.getNoteOther());
    //                 reimburseApp.setNotePekerjaan(existingAbsenEntity.getNotePekerjaan());
    //                 reimburseApp.setNotePlgCepat(existingAbsenEntity.getNotePlgCepat());
    //                 reimburseApp.setNoteTelatMsk(existingAbsenEntity.getNoteTelatMsk());
    //                 reimburseApp.setProjectId(existingAbsenEntity.getProjectId());
    //                 reimburseApp.setTglAbsen(existingAbsenEntity.getTglAbsen());
    //                 reimburseApp.setTotalJamKerja(totalHours);
    //                 reimburseApp.setUsrUpd(nama);
    //                 reimburseAppRepository.save(reimburseApp);

    //                 Map<String, Object> response = new HashMap<>();
    //                 response.put("success", true);
    //                 response.put("message", "Absen data inserted successfully + timesheet + reimburse");
    //                 response.put("data", existingAbsenEntity);
    
    //                 return ResponseEntity.status(HttpStatus.OK).body(response);
    //             }else{
    //                 Map<String, Object> response = new HashMap<>();
    //                 response.put("success", false);
    //                 response.put("message", "You have already submitted absen data for today");
    //                 response.put("data", existingAbsenRecords.get(0));
    //                 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    //             }
    //         } else {
    //             Map<String, Object> response = new HashMap<>();
    //             response.put("success", false);
    //             response.put("message", "Invalid token format");
    //             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    //         }
    //     } catch (Exception e) {
    //         Map<String, Object> response = new HashMap<>();
    //         response.put("success", false);
    //         response.put("message", "Failed to insert absen data");
    //         response.put("error", e.getMessage());
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    //     }
    // }

    public ResponseEntity<Map<String, Object>> inputAbsenBelumPulang(@RequestHeader("Authorization") String tokenWithBearer, @RequestParam("id") Long idAbsen, AbsenRequest request) {
        try {
                if (tokenWithBearer.startsWith("Bearer ")){
                String token = tokenWithBearer.substring("Bearer ".length());
                String nik = jwtService.extractUsername(token);
                String nama = karyawanRepository.findNamaByNik(nik);
                Optional<SysUserEntity> sysUser = sysUserRepository.findByUserId(nik);
                AbsenEntity existingAbsenData = absenRepository.findByIdAbsen(idAbsen);
                LocalDateTime sekarangBanget = LocalDateTime.now();
                Timestamp jamIni = Timestamp.valueOf(sekarangBanget);

                if (sysUser.isPresent()) {
                    SysUserEntity sysUserEntity = sysUser.get();
                    String jabatanId = sysUserEntity.getRole().getJabatanId();

                    if("HEAD".equals(jabatanId)) {
                        existingAbsenData.setNotePekerjaan(request.getNotePekerjaan());
                        existingAbsenData.setLokasiPlg(request.getLokasiPlg());
                        existingAbsenData.setJarakPlg(request.getJarakPlg());
                        existingAbsenData.setJamPlg(request.getJamPlg());
                        LocalTime jamMsk = existingAbsenData.getJamMsk();
                        LocalTime jamPlg = request.getJamPlg();
                        if (jamMsk != null && jamPlg != null) {
                            BigDecimal hoursPart = new BigDecimal(Duration.between(jamMsk, jamPlg).toHoursPart());
                            existingAbsenData.setTotalJamKerja(hoursPart);
                            if (hoursPart.compareTo(new BigDecimal(9))>0) {
                                existingAbsenData.setIsAbsen("1");
                                existingAbsenData.setIsLembur("1");
                            } else {
                                existingAbsenData.setIsAbsen("1");
                                existingAbsenData.setIsLembur("0");
                            }
                        }
                        if (request.getNoteOther() != null) {
                            existingAbsenData.setNoteOther(request.getNoteOther());
                            existingAbsenData.setIsOther("1");
                        }
                        absenRepository.save(existingAbsenData);

                        // Ke Timesheet
                        TimesheetEntity timesheetEntity = new TimesheetEntity();

                        timesheetEntity.setDtmCrt(jamIni);
                        String flgKetValue = "-"; // Default value

                            if ("1".equals(existingAbsenData.getIsSakit())) {
                                flgKetValue = "sakit";
                            } else if ("1".equals(existingAbsenData.getIsCuti())) {
                                flgKetValue = "cuti";
                            }

                        timesheetEntity.setFlgKet(flgKetValue);
                        timesheetEntity.setHari(existingAbsenData.getHari());
                        timesheetEntity.setJamKeluar(request.getJamPlg());
                        timesheetEntity.setJamMasuk(jamMsk);
                        timesheetEntity.setNama(nama);
                        timesheetEntity.setNik(nik);
                        timesheetEntity.setNote(request.getNotePekerjaan());
                        timesheetEntity.setProjectId(request.getProjectId());
                        timesheetEntity.setTglMsk(existingAbsenData.getTglAbsen());
                        timesheetEntity.setUsrCrt(nama);

                        LocalTime jamMashook = jamMsk;
                        LocalTime jamPoelang = request.getJamPlg();
                        
                        BigDecimal totalHours = BigDecimal.ZERO;
                        BigDecimal jamLembur = BigDecimal.ZERO;

                        if (jamMashook != null && jamPoelang != null){
                        Duration duration = Duration.between(jamMashook, jamPoelang);
                        long totalHoursLong = duration.toHours();
                        totalHours = BigDecimal.valueOf(totalHoursLong);
                            if (totalHoursLong > 9){
                                jamLembur = BigDecimal.valueOf(totalHoursLong-9);
                            }
                        }

                        timesheetEntity.setOvertime(jamLembur);
                        timesheetEntity.setTotalJamKerja(totalHours);
                        timesheetEntity.setProjectId(existingAbsenData.getProjectId());
                        timesheetEntity.setDtmCrt(jamIni);
                        timesheetRepository.save(timesheetEntity);
                        Map<String, Object> response = new HashMap<>();
                        response.put("success", true);
                        response.put("message", "Absen Lupa Pulang Berhasil");
                        response.put("data", existingAbsenData);

                        return ResponseEntity.status(HttpStatus.OK).body(response);
                    } else {
                        // ngga perlu cek, langsung tarik dari screen.
                        AbsenPulangAppEntity lupaPulang = new AbsenPulangAppEntity();
                        lupaPulang.setId(idAbsen);
                        lupaPulang.setProjectId(existingAbsenData.getProjectId());
                        lupaPulang.setJamMsk(existingAbsenData.getJamMsk());
                        lupaPulang.setJarakMsk(existingAbsenData.getJarakMsk());
                        lupaPulang.setLokasiMsk(existingAbsenData.getLokasiMsk());
                        lupaPulang.setNoteTelatMsk(existingAbsenData.getNoteTelatMsk());
                        lupaPulang.setGpsLatitudeMsk(existingAbsenData.getGpsLatitudeMsk());
                        lupaPulang.setGpsLongitudeMsk(existingAbsenData.getGpsLongitudeMsk());
                        lupaPulang.setNama(nama);
                        lupaPulang.setNik(nik);
                        // bagian yang dapet dari front-end request
                        lupaPulang.setKeterangan(request.getKeteranganLupaPulang());
                        lupaPulang.setNotePekerjaan(request.getNotePekerjaan());
                        lupaPulang.setLokasiPlg(request.getLokasiPlg());
                        lupaPulang.setGpsLatitudePlg(request.getGpsLatitudePlg());
                        lupaPulang.setGpsLongitudePlg(request.getGpsLongitudePlg());

                        lupaPulang.setFlagApp(null);
                        lupaPulang.setJamPlg(request.getJamPlg());
                        lupaPulang.setJarakPlg(request.getJarakPlg());
                        // dari db dan dari server
                        lupaPulang.setDtmUpd(Timestamp.valueOf(LocalDateTime.now()));
                        lupaPulang.setGpsLatitudeMsk(existingAbsenData.getGpsLatitudeMsk());
                        lupaPulang.setHari(existingAbsenData.getHari());
                        lupaPulang.setGpsLongitudeMsk(existingAbsenData.getGpsLongitudeMsk());
                        lupaPulang.setJamMsk(existingAbsenData.getJamMsk());
                        lupaPulang.setLokasiMsk(existingAbsenData.getLokasiMsk());
                        lupaPulang.setTglAbsen(existingAbsenData.getTglAbsen());
                        /*
                        data yang harus lo kasih:
                        KeteranganLupaPulang
                        NotePekerjaan
                        LokasiPlg
                        GpsLatitudePlg
                        GpsLongitudePlg
                        Hari
                        JamPlg
                        JarakPlg
                        */

                        absenPulangAppRepository.save(lupaPulang);

                        Map<String, Object> response = new HashMap<>();
                        response.put("success", true);
                        response.put("message", "Absen Lupa Pulang Berhasil");
                        response.put("data", lupaPulang);


                        return ResponseEntity.status(HttpStatus.OK).body(response);
                    }
                } else {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("message", "User tidak ditemukan");

                    return ResponseEntity.status(HttpStatus.OK).body(response);
                }
                
            
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Invalid token format");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to insert absen data");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    public ResponseEntity<Map<String, Object>> updateAbsen(@RequestHeader("Authorization") String tokenWithBearer, AbsenRequest request) {
        try {
            // Extracting NIK from the token
            if (tokenWithBearer.startsWith("Bearer ")) {
                String token = tokenWithBearer.substring("Bearer ".length());
                String nik = jwtService.extractUsername(token);
                
                // Check if NIK is valid
                if (nik != null) {
                    // Retrieve KaryawanEntity by NIK
                    LocalDate currentDate = LocalDate.now();
                    List<AbsenEntity> existingAbsenEntityList = absenRepository.findByNikAndTglAbsen(nik, currentDate);
                    String karyawanEntities = karyawanRepository.findNamaByNik(nik);
                    String nama = karyawanEntities;
                    AbsenEntity existingAbsenEntity = existingAbsenEntityList.get(0);
                    AbsenTrackingEntity trackingAbsenEntity = new AbsenTrackingEntity();
                    if("1".equals(request.getIsOther())){
                        LocalTime jamSekarang = LocalTime.parse(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                        existingAbsenEntity.setLokasiMsk(request.getLokasiMsk());
                        existingAbsenEntity.setIsOther("1");
                        existingAbsenEntity.setProjectId(null);

                        AbsenTrackingEntity absenTrackingEntity = new AbsenTrackingEntity();
                        absenTrackingEntity.setNik(nik);
                        absenTrackingEntity.setGpsLongitudeMsk(request.getGpsLongitudeMsk());
                        absenTrackingEntity.setGpsLongitudeMsk(request.getGpsLongitudeMsk());
                        absenTrackingEntity.setJamMsk(jamSekarang);
                        absenTrackingEntity.setNama(nama);
                        absenTrackingEntity.setIsOther("1");
                        absenTrackingEntity.setTglAbsen(currentDate);
                        absenTrackingEntity.setNoteOther(request.getNoteOther());
                        absenTrackingEntity.setUsrCrt(nama); 
                        absenTrackingEntity.setDtmCrt(Timestamp.valueOf(LocalDateTime.now()));
                        absenTrackingEntity.setIsOther(request.getIsOther());
                        absenTrackingRepository.save(absenTrackingEntity);

                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("message", "Special absen data updated and tracked successfully");
                    response.put("data", absenTrackingEntity);

                    return ResponseEntity.status(HttpStatus.OK).body(response);
                    }
                    if (karyawanEntities.isEmpty()) {
                        // NIK not found in KaryawanEntity
                        Map<String, Object> response = new HashMap<>();
                        response.put("success", false);
                        response.put("message", "NIK not found in KaryawanEntity. Please contact the administrator.");
                        response.put("niknya", nik);
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                    }
                    
                    // Retrieve registered projects for the user
                    List<PenempatanEntity> registeredProjects = penempatanRepository.findAllByNik(nik);
                    boolean projectFound = false;
                    
                    // Check if the requested project is among the registered projects
                    for (PenempatanEntity penempatanEntity : registeredProjects) {
                        if (penempatanEntity.getProjectId().getProjectId().equals(request.getProjectId().getProjectId().toString())) {
                            projectFound = true;
                            // break;
                        }
                    }
                    
                    if (projectFound) {
                        // Project found, proceed to update absen data
                        
                        if (!existingAbsenEntityList.isEmpty()) {
                            // Update the existing AbsenEntity
                            LocalTime jamSekarang = LocalTime.parse(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                            existingAbsenEntity.setJarakMsk(request.getJarakMsk());
                            existingAbsenEntity.setProjectId(request.getProjectId());
                            existingAbsenEntity.setLokasiMsk(request.getLokasiMsk());
                            
                            // Save the updated AbsenEntity
                            absenRepository.save(existingAbsenEntity);
                            
                            // Submitting the updated value to the tbl_absen_tracking
                            trackingAbsenEntity.setDtmCrt(Timestamp.valueOf(LocalDateTime.now()));
                            trackingAbsenEntity.setProjectId(request.getProjectId());
                            trackingAbsenEntity.setNik(nik);
                            trackingAbsenEntity.setTglAbsen(currentDate);
                            trackingAbsenEntity.setGpsLatitudeMsk(request.getGpsLatitudeMsk());
                            trackingAbsenEntity.setGpsLongitudeMsk(request.getGpsLongitudeMsk());
                            trackingAbsenEntity.setJamMsk(jamSekarang);
                            trackingAbsenEntity.setHari(getIndonesianDayOfWeek(LocalDate.now().getDayOfWeek()));
                            trackingAbsenEntity.setJarakMsk(request.getJarakMsk());
                            trackingAbsenEntity.setLokasiMsk(request.getLokasiMsk());
                            trackingAbsenEntity.setNama(nama);
                            trackingAbsenEntity.setNoteTelatMsk(request.getNoteTelatMsk());

                            // save it to the tracking table
                            absenTrackingRepository.save(trackingAbsenEntity);


                            Map<String, Object> response = new HashMap<>();
                            response.put("success", true);
                            response.put("message", "Absen data updated successfully");
                            response.put("data", trackingAbsenEntity);
                            
                            return ResponseEntity.status(HttpStatus.OK).body(response);
                        } else {
                            // Absen data not found for the current date
                            Map<String, Object> response = new HashMap<>();
                            response.put("success", false);
                            response.put("message", "Absen data not found for the current date");
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                        }
                    } else {
                        // User is not authorized to access this project
                        Map<String, Object> response = new HashMap<>();
                        response.put("success", false);
                        response.put("message", "You are not authorized to access this project");
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
                    }
                }
            } else {
                // Invalid token format
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Invalid token format");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            // Error occurred
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to update absen data");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    
        // Default return statement for unexpected cases
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", "Unexpected error occurred");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
    public ResponseEntity<Map<String, Object>> getAbsenBelumPulang(String tokenWithBearer) {
        try {
            if (tokenWithBearer.startsWith("Bearer ")) {
                String token = tokenWithBearer.substring("Bearer ".length());
                String nik = jwtService.extractUsername(token);

                // Search for idAbsen that has no Plg data`
                List<AbsenEntity> unprocessedAbsenList = absenRepository.findIdAbsenByNikAndIsAbsenIsNullAndJamMskIsNotNull(nik);
                List<AbsenPulangAppEntity> processedAbsenList = absenPulangAppRepository.findAll();
                unprocessedAbsenList.removeIf(unprocessedAbsen -> processedAbsenList.stream()
                .anyMatch(processedAbsen -> Objects.equals(processedAbsen.getTglAbsen(), unprocessedAbsen.getTglAbsen())));
                System.out.println("ini data belom absennya: "+unprocessedAbsenList);
                List<AbsenBelumPulangResponse> listAbsenResponse = new ArrayList<>();
                for(AbsenEntity absenEntity : unprocessedAbsenList){
                    ProjectEntity projectId = absenEntity.getProjectId();
                    Double longitudeProject = null;
                    Double latitudeProject = null;
                    if (projectId != null){
                        longitudeProject = projectId.getGpsLongitude();
                        latitudeProject = projectId.getGpsLatitude();
                    }
                // Check if projectId is null
                String projectName = (projectId != null) ? projectId.getNamaProject() : "Unknown Project";

                // Handle other potential null values in a similar manner
                String noteTelatMsk = absenEntity.getNoteTelatMsk() != null ? absenEntity.getNoteTelatMsk() : "No Note";
                LocalTime jamMsk = absenEntity.getJamMsk() != null ? absenEntity.getJamMsk() : LocalTime.MIN;
                String lokasiProject = absenEntity.getLokasiMsk() != null ? absenEntity.getLokasiMsk() : "Unknown Location";
                LocalDate tglAbsen = absenEntity.getTglAbsen() != null ? absenEntity.getTglAbsen() : LocalDate.MIN;
                Long idAbsen = absenEntity.getId();  // Assuming ID is always non-null

                AbsenBelumPulangResponse response = new AbsenBelumPulangResponse(projectName, noteTelatMsk, jamMsk, lokasiProject, tglAbsen, idAbsen, longitudeProject, latitudeProject);
                listAbsenResponse.add(response);
                }
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Unprocessed Absen data retrieved successfully");
                response.put("data", listAbsenResponse);

                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Invalid token format");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to retrieve unprocessed Absen data");
            response.put("error", e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    public ResponseEntity<Map<String, Object>> otherAbsen(@RequestHeader("Authorization") String tokenWithBearer, AbsenRequest request) {
        try {
            // Extracting NIK from the token
            if (tokenWithBearer.startsWith("Bearer ")) {
                String token = tokenWithBearer.substring("Bearer ".length());
                String nik = jwtService.extractUsername(token);
                
                // Check if NIK is valid
                if (nik != null) {
                    // Retrieve KaryawanEntity by NIK
                    String karyawanEntities = karyawanRepository.findNamaByNik(nik);
                    
                    if (karyawanEntities.isEmpty()) {
                        // NIK not found in KaryawanEntity
                        Map<String, Object> response = new HashMap<>();
                        response.put("success", false);
                        response.put("message", "NIK not found in KaryawanEntity. Please contact the administrator.");
                        response.put("niknya", nik);
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                    }
                    
                    // Extracting the user's name from KaryawanEntity (just in case)
                    String nama = karyawanEntities;
                    
                    // Retrieve registered projects for the user
                    List<PenempatanEntity> registeredProjects = penempatanRepository.findAllByNik(nik);
                    boolean projectFound = false;
                    
                    // Check if the requested project is among the registered projects
                    for (PenempatanEntity penempatanEntity : registeredProjects) {
                        if (penempatanEntity.getProjectId().getProjectId().equals(request.getProjectId().getProjectId().toString())) {
                            projectFound = true;
                            // break;
                        }
                    }
                    
                    if (projectFound) {
                        // Project found, proceed to update absen data
                        LocalDate currentDate = LocalDate.now();
                        List<AbsenEntity> existingAbsenEntityList = absenRepository.findByNikAndTglAbsen(nik, currentDate);
                        AbsenEntity existingAbsenEntity = existingAbsenEntityList.get(0);
                        AbsenTrackingEntity trackingAbsenEntity = new AbsenTrackingEntity();
                        if (!existingAbsenEntityList.isEmpty()) {
                            // Update the existing AbsenEntity
                            // LocalTime jamSekarang = LocalTime.parse(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                            LocalTime jamSekarang = LocalTime.now();
                            existingAbsenEntity.setJamMsk(jamSekarang);
                            existingAbsenEntity.setJarakMsk(request.getJarakMsk());
                            existingAbsenEntity.setGpsLatitudeMsk(request.getGpsLatitudeMsk());
                            existingAbsenEntity.setGpsLongitudeMsk(request.getGpsLongitudeMsk());
                            
                            // Save the updated AbsenEntity
                            absenRepository.save(existingAbsenEntity);
                            
                            // Submitting the updated value to the tbl_absen_tracking
                            trackingAbsenEntity.setDtmCrt(Timestamp.valueOf(LocalDateTime.now()));
                            trackingAbsenEntity.setGpsLatitudeMsk(request.getGpsLatitudeMsk());
                            trackingAbsenEntity.setGpsLongitudeMsk(request.getGpsLongitudeMsk());
                            trackingAbsenEntity.setJamMsk(jamSekarang);
                            trackingAbsenEntity.setHari(getIndonesianDayOfWeek(LocalDate.now().getDayOfWeek()));
                            trackingAbsenEntity.setJarakMsk(request.getJarakMsk());
                            trackingAbsenEntity.setLokasiMsk(request.getLokasiMsk());
                            trackingAbsenEntity.setNama(nama);
                            trackingAbsenEntity.setNoteTelatMsk(request.getNoteTelatMsk());

                            // save it to the tracking table
                            absenTrackingRepository.save(trackingAbsenEntity);


                            Map<String, Object> response = new HashMap<>();
                            response.put("success", true);
                            response.put("message", "Absen data updated successfully");
                            response.put("data", trackingAbsenEntity);
                            
                            return ResponseEntity.status(HttpStatus.OK).body(response);
                        } else {
                            // Absen data not found for the current date
                            Map<String, Object> response = new HashMap<>();
                            response.put("success", false);
                            response.put("message", "Absen data not found for the current date");
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                        }
                    } else {
                        // User is not authorized to access this project
                        Map<String, Object> response = new HashMap<>();
                        response.put("success", false);
                        response.put("message", "You are not authorized to access this project");
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
                    }
                }
            } else {
                // Invalid token format
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Invalid token format");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            // Error occurred
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to update absen data");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    
        // Default return statement for unexpected cases
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", "Unexpected error occurred");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    public ResponseEntity <Map<String, Object>> getIsAbsen(@RequestHeader("Authorization") String tokenWithBearer, LocalDate hariIni) {
        try{
            if (tokenWithBearer.startsWith("Bearer ")) {
                String token = tokenWithBearer.substring("Bearer ".length());
                // keluarin isOther, 
                String nik = jwtService.extractUsername(token);
                LocalDate hariIniBanget = hariIni.now();
                Map<String, Object> response = new HashMap<>();
                if (nik != null) {
                    // check absen masuk nik tersebut di hari ini
                    List<AbsenEntity> kondisiAbsen = absenRepository.findByNikAndTglAbsen(nik, hariIniBanget);
                    if (kondisiAbsen.isEmpty()) {
                        response.put("success", true);
                        response.put("message", "data absen masih kosong untuk hari ini");
                        response.put("data", kondisiAbsen);
                        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
                    }else{
                    response.put("success", false);
                    response.put("message", "data absen sudah terisi");
                    response.put("data", kondisiAbsen);
                    return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(response);
                    }
                } else {
                    response.put("success", false);
                    response.put("message", "nik tidak dapat ditemukan");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                }
            } else {
                // Invalid token format
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Invalid token format");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            e.printStackTrace();

            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "terjadi kesalahan dalam memproses permintaan");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    public ResponseEntity <Map<String, Object>> getAllCuti(@RequestHeader("Authorization") String tokenWithBearer) {
        try{
            if (tokenWithBearer.startsWith("Bearer ")) {
                String token = tokenWithBearer.substring("Bearer ".length());
                String nik = jwtService.extractUsername(token);
                Map<String, Object> response = new HashMap<>();
                if (nik != null) {
                    // check absen masuk nik tersebut di hari ini
                    List<AbsenEntity> dataCuti = absenRepository.findByIsCuti("1");
                    if (dataCuti.isEmpty()) {
                        response.put("success", true);
                        response.put("message", "data cuti tidak ditemukan");
                        response.put("data", null);
                        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
                    }else{
                    response.put("success", false);
                    response.put("message", "data cuti berhasil diretrieve");
                    response.put("data", dataCuti);
                    return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(response);
                    }
                } else {
                    response.put("success", false);
                    response.put("message", "nik tidak dapat ditemukan");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                }
            } else {
                // Invalid token format
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Invalid token format");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            e.printStackTrace();

            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "terjadi kesalahan dalam memproses permintaan");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    public ResponseEntity <Map<String, Object>> getCutiByDate(@RequestHeader("Authorization") String tokenWithBearer, @RequestParam("date") String dateString) {
        try{
            if (tokenWithBearer.startsWith("Bearer ")) {
                String token = tokenWithBearer.substring("Bearer ".length());
                String nik = jwtService.extractUsername(token);
                Map<String, Object> response = new HashMap<>();
                if (nik != null) {
                    // check absen masuk nik tersebut di hari ini
                    LocalDate date = LocalDate.parse(dateString);
                    List<CutiEntity> dataCuti = cutiRepository.findByIsApprovedAndTglMulai("1", date);
                    if (dataCuti.isEmpty()) {
                        response.put("success", true);
                        response.put("message", "data cuti tidak ditemukan");
                        response.put("data", null);
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                    }else{
                    response.put("success", false);
                    response.put("message", "data cuti berhasil diretrieve");
                    response.put("data", dataCuti);
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                    }
                } else {
                    response.put("success", false);
                    response.put("message", "nik tidak dapat ditemukan");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                }
            } else {
                // Invalid token format
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Invalid token format");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            e.printStackTrace();

            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "terjadi kesalahan dalam memproses permintaan");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    public ResponseEntity<Map<String, Object>> masukinNama(String tokenWithBearer) {
        List<AbsenEntity> dataSemuaAbsen = absenRepository.findAll();
        for (AbsenEntity dataAbsen : dataSemuaAbsen){
            String nik = dataAbsen.getNik();
            String nama = karyawanRepository.findNamaByNik(nik);
            dataAbsen.setNama(nama);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("success", "yoi");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}