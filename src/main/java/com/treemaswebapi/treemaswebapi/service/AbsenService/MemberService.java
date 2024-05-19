package com.treemaswebapi.treemaswebapi.service.AbsenService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.treemaswebapi.treemaswebapi.config.JwtService;
import com.treemaswebapi.treemaswebapi.controller.AbsenController.AbsenResponse;
import com.treemaswebapi.treemaswebapi.controller.MemberController.request.MemberRequest;
import com.treemaswebapi.treemaswebapi.entity.AbsenEntity.AbsenEntity;
import com.treemaswebapi.treemaswebapi.entity.AbsenEntity.AbsenImgEntity;
import com.treemaswebapi.treemaswebapi.entity.AbsenEntity.AbsenTrackingEntity;
import com.treemaswebapi.treemaswebapi.entity.AbsenEntity.AbsenTrackingData.AbsenTrackingData;
import com.treemaswebapi.treemaswebapi.entity.KaryawanEntity.KaryawanImageEntity;
import com.treemaswebapi.treemaswebapi.entity.PenempatanEntity.PenempatanEntity;
import com.treemaswebapi.treemaswebapi.entity.ProjectEntity.ProjectDetails;
import com.treemaswebapi.treemaswebapi.entity.ProjectEntity.ProjectEntity;
import com.treemaswebapi.treemaswebapi.repository.AbsenImgRepository;
import com.treemaswebapi.treemaswebapi.repository.AbsenRepository;
import com.treemaswebapi.treemaswebapi.repository.AbsenTrackingRepository;
import com.treemaswebapi.treemaswebapi.repository.KaryawanImageRepository;
import com.treemaswebapi.treemaswebapi.repository.PenempatanRepository;
import com.treemaswebapi.treemaswebapi.repository.ProjectRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final PenempatanRepository penempatanRepository;
    private final AbsenImgRepository absenImgRepository;
    private final AbsenRepository absenRepository;
    private final AbsenTrackingRepository absenTrackingRepository;
    private final JwtService jwtService;
    private final ProjectRepository projectRepository;
    private final KaryawanImageRepository karyawanImageRepository;


    // fungsi untuk narik data project mana aja yang dipegang si leader
    // fungsi ini difilter bukan dari backend, tapi dari front-end
    public ResponseEntity<Map<String, Object>> leaderProjectDetails(@RequestHeader String tokenWithBearer, @RequestParam("projectId") String projectIdReq) {
        try {
            if (tokenWithBearer.startsWith("Bearer ")) {
                String token = tokenWithBearer.substring("Bearer ".length());
                String nik = jwtService.extractUsername(token);
    
                // Retrieve project IDs associated with the 'nik'
                List<ProjectDetails> projectIds = retrieveProjectDetailsForNik(nik);
    
                if (!projectIds.isEmpty()) {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("message", "Project IDs retrieved successfully");
                    response.put("data", projectIds);
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                } else {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", false);
                    response.put("message", "No project IDs found for the 'nik'");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                }
            } else {
                // Handle the case where the token format is invalid
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Invalid token format");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to retrieve project IDs");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    private List<ProjectDetails> retrieveProjectDetailsForNik(String nik) {
    try {
        List<PenempatanEntity> penempatanEntities = penempatanRepository.findByNik(nik);

        List<ProjectDetails> projectDetails = new ArrayList<>();

        for (PenempatanEntity penempatanEntity : penempatanEntities) {
            String projectId = penempatanEntity.getProjectId().toString();
            ProjectEntity project = projectRepository.findByProjectId(projectId);

            if (project != null) {
                ProjectDetails details = new ProjectDetails();
                details.setProjectId(project.getProjectId().toString());
                details.setProjectName(project.getNamaProject());
                projectDetails.add(details);
            }
        }

        return projectDetails;
    } catch (Exception e) {
        return new ArrayList<>();
    }
}



    // fungsi untuk narik data absen secara keseluruhan dengan projectId yang udah dipilih dari front-end
    public ResponseEntity<Map<String, Object>> getAbsenFromProjectId(@RequestHeader("Authorization") String tokenWithBearer, @RequestBody MemberRequest request) {
        try {
            if (tokenWithBearer.startsWith("Bearer ")) {
                List<AbsenEntity> absenEntities = absenRepository.findAllByProjectIdAndTglAbsen(request.getProjectId(), request.getTargetDate());

                if (!absenEntities.isEmpty()) {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("message", "Absen data retrieved successfully");
                    response.put("data", absenEntities);
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                } else {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", false);
                    response.put("message", "No Absen data found for the provided projectId");

                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                }
            } else {
                // Handle the case where the token is not valid
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Invalid token format");
                
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to retrieve Absen data");
            response.put("error", e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // this function serve as a method to get all the projects that we have. and it could be accessed by everyone in the company
        public ResponseEntity<Map<String, Object>> projectDetails(@RequestHeader String tokenWithBearer) {
            try {
                if (tokenWithBearer.startsWith("Bearer ")) {
                    String token = tokenWithBearer.substring("Bearer ".length());
                    String nik = jwtService.extractUsername(token);
                    System.out.println("kesini " + nik);
        
                    List<ProjectEntity> allProjects = projectRepository.findAll();
                    List<ProjectDetails> projectDetailsList = new ArrayList<>();
        
                    for (ProjectEntity project : allProjects) {
                        if (project != null) {
                            ProjectDetails details = new ProjectDetails();
                            PenempatanEntity penempatan = penempatanRepository.findActiveByProjectIdAndNik(project, nik);
                            System.out.println("INI PENEMPATANNYA: "+ penempatan);
                            details.setProjectId(project.getProjectId().toString());
                            details.setProjectName(project.getNamaProject());
                            details.setProjectAddress(project.getLokasi());
                            details.setJrkMax(project.getJrkMax());
                            details.setGpsLongitude(project.getGpsLongitude());
                            details.setGpsLatitude(project.getGpsLatitude());
                            details.setJamMasuk(project.getJamMasuk());
                            details.setJamKeluar(project.getJamKeluar());
                            if (penempatan != null) {
                                details.setActive(penempatan.getActive());
                            } else {
                                details.setActive(null); // or any default value you want to set
                            }                    
                            projectDetailsList.add(details);
                        }
                    }
        
                    if (!projectDetailsList.isEmpty()) {
                        Map<String, Object> response = new HashMap<>();
                        response.put("success", true);
                        response.put("message", "Project details retrieved successfully");
                        response.put("data", projectDetailsList);
                        return ResponseEntity.status(HttpStatus.OK).body(response);
                    } else {
                        Map<String, Object> response = new HashMap<>();
                        response.put("success", false);
                        response.put("message", "No project details found for the 'nik'");
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                    }
                } else {
                    // Handle the case where the token format is invalid
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", false);
                    response.put("message", "Invalid token format");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                }
            } catch (Exception e) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Failed to retrieve project details");
                response.put("error", e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        }

        public ResponseEntity<Map<String, Object>> getAbsenMemberByProjectId(@RequestHeader("Authorization") String tokenWithBearer, @RequestParam("projectId") ProjectEntity projectId) {
            try {
                if (tokenWithBearer.startsWith("Bearer ")) {
                    String token = tokenWithBearer.substring("Bearer ".length());
                    String nik = jwtService.extractUsername(token);
                    System.out.println(nik);
                    LocalDate date = LocalDate.now();
                    //mau narik data yang ada di penempatanEntity, cari by projectId
                    List<PenempatanEntity> listMember = penempatanRepository.findByProjectIdAndActive(projectId, "1");
                    List<AbsenEntity> listAbsen = absenRepository.findByTglAbsen(date);
                    List<Object> responseList = new ArrayList<>();
                    System.out.println(listMember);
                    System.out.println(listAbsen);
                    for (PenempatanEntity member : listMember) {
                        AbsenEntity absenData = listAbsen.stream()
                                                         .filter(absen -> absen.getNik().equals(member.getNik()))
                                                         .findFirst()
                                                         .orElse(null);
            
                        if (absenData != null) {
                            responseList.add(absenData);
                        } else {
                            // Placeholder or handling for members without fully filled absence data
                            responseList.add("Absent data not fully filled for member with NIK: " + member.getNik());
                        }
                    }
                    Map<String, Object> response = new HashMap<>();
                    if (responseList.isEmpty()) {
                        response.put("success", false);
                        response.put("message", "belum ada member");
                    } else {
                        response.put("success", true);
                        response.put("message", "berhasil retrieve data member");
                    }
                    response.put("data", responseList);
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                } else {
                    // Handle the case where the token format is invalid
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", false);
                    response.put("message", "Invalid token format");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                }
            } catch (Exception e) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Failed to retrieve project details");
                response.put("error", e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        }
        
        public ResponseEntity<Map<String, Object>> getDataAbsenMember(@RequestHeader("Authorization") String tokenWithBearer, @RequestParam("nik") String nik) {
            try {
                if (tokenWithBearer.startsWith("Bearer ")) {
                    String token = tokenWithBearer.substring("Bearer ".length());
                    String nikUser = jwtService.extractUsername(token);
                    System.out.println("ini user yang lagi loginnya"+nikUser);
                    LocalDate currentDate = LocalDate.now();
                    List<AbsenEntity> dataAbsenDb = absenRepository.findByNikAndTglAbsen(nik, currentDate);
                    Long idAbsenLong = dataAbsenDb.isEmpty() ? null : dataAbsenDb.get(0).getId();
                    System.out.println("ini id absen orang yang dicari"+idAbsenLong);
                    //mau narik data yang ada di penempatanEntity, cari by projectId
                    List<AbsenTrackingEntity> dataAbsenTrackingDb = absenTrackingRepository.findByTglAbsenAndNik(currentDate, nik);
                    System.out.println("ini data absen Tracking-nya"+dataAbsenTrackingDb);
                    
                    String dataAbsenImg = null;
                    Optional<AbsenImgEntity> optionalImage = absenImgRepository.findById(idAbsenLong);
                    if (optionalImage.isPresent()) {
                        dataAbsenImg = optionalImage.get().getImage64();
                    }
                    String karyawanImage = null;
                    Optional<KaryawanImageEntity> optionalGambardia = karyawanImageRepository.findByNik(nik);
                    if (optionalGambardia.isPresent()) {
                        karyawanImage = optionalGambardia.get().getFoto();
                    }
                    System.out.println("ini data gambarnya"+absenImgRepository.findAll().get(0).getId());

                    AbsenTrackingData absenTrackingData = new AbsenTrackingData();
                    if (dataAbsenTrackingDb.isEmpty()){
                        absenTrackingData.setNik(dataAbsenDb.get(0).getNik());
                        absenTrackingData.setNama(dataAbsenDb.get(0).getNama());
                        }else{
                        AbsenTrackingEntity firstAbsenTrackingEntity = dataAbsenTrackingDb.get(0);
                        AbsenTrackingEntity secondAbsenTrackingEntity = dataAbsenTrackingDb.size() > 1 ? dataAbsenTrackingDb.get(1) : null;
                        AbsenTrackingEntity thirdAbsenTrackingEntity = dataAbsenTrackingDb.size() > 2 ? dataAbsenTrackingDb.get(2) : null;
                        
                        List<Double> gpsLatitudeMskList = new ArrayList<>();
                        List<Double> gpsLongitudeMskList = new ArrayList<>();
                        
                        gpsLatitudeMskList.add(firstAbsenTrackingEntity.getGpsLatitudeMsk());
                        gpsLongitudeMskList.add(firstAbsenTrackingEntity.getGpsLongitudeMsk());
                        
                        if(secondAbsenTrackingEntity != null){
                        gpsLatitudeMskList.add(secondAbsenTrackingEntity.getGpsLatitudeMsk());
                        gpsLongitudeMskList.add(secondAbsenTrackingEntity.getGpsLongitudeMsk());
                        }

                        if(thirdAbsenTrackingEntity != null){
                        gpsLatitudeMskList.add(thirdAbsenTrackingEntity.getGpsLatitudeMsk());
                        gpsLongitudeMskList.add(thirdAbsenTrackingEntity.getGpsLongitudeMsk());    
                        }
                        
                        absenTrackingData.setGpsLatitudeMsk(gpsLatitudeMskList);
                        absenTrackingData.setGpsLongitudeMsk(gpsLongitudeMskList);
                        absenTrackingData.setGpsLatitudePlg(dataAbsenDb.get(0).getGpsLatitudePlg());
                        absenTrackingData.setGpsLongitudePlg(dataAbsenDb.get(0).getGpsLongitudePlg());
                        absenTrackingData.setJamMsk(dataAbsenDb.get(0).getJamMsk());
                        absenTrackingData.setCatatanPlgCpt(dataAbsenDb.get(0).getNotePlgCepat());
                        absenTrackingData.setJamPlg(dataAbsenDb.get(0).getJamPlg());
                        absenTrackingData.setNama(dataAbsenDb.get(0).getNama());
                        absenTrackingData.setNamaProject(dataAbsenDb.get(0).getProjectId().getNamaProject());
                        absenTrackingData.setNik(dataAbsenDb.get(0).getNik());
                        absenTrackingData.setNotePekerjaan(dataAbsenDb.get(0).getNotePekerjaan());
                        absenTrackingData.setCatatanTelat(dataAbsenDb.get(0).getNoteTelatMsk());
                        }
                    AbsenResponse absenResponse = new AbsenResponse();
                    absenResponse.setAbsenTrackingData(absenTrackingData);
                    absenResponse.setAbsenImg(dataAbsenImg != null ? dataAbsenImg : null);
                    absenResponse.setPp(karyawanImage != null ? karyawanImage : null);
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("message", "BERHASIL MENDAPATKAN DATA SEORANG MEMBER");
                    response.put("data", absenResponse);
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                } else {
                    // Handle the case where the token format is invalid
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", false);
                    response.put("message", "Invalid token format");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                }
            } catch (Exception e) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Failed to retrieve data absen member");
                response.put("error", e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        }

        public ResponseEntity<Map<String, Object>> getProject(String tokenWithBearer) {
            try {
                if (tokenWithBearer.startsWith("Bearer ")) {
                    String token = tokenWithBearer.substring("Bearer ".length());
                    String nik = jwtService.extractUsername(token);
        
                    List<PenempatanEntity> projectPenempatan = penempatanRepository.findByActiveAndNik("1", nik);
        
                    Map<String, Object> response = new HashMap<>();
                    if (!projectPenempatan.isEmpty()) {
                        List<ProjectDetails> projectDetailsList = new ArrayList<>();
        
                        for (PenempatanEntity penempatanSiLeader : projectPenempatan) {
                            ProjectEntity project = penempatanSiLeader.getProjectId();
                            if (project != null) {
                                ProjectDetails details = new ProjectDetails();
                                PenempatanEntity penempatan = penempatanRepository.findActiveByProjectIdAndNik(project, nik);
        
                                details.setProjectId(project.getProjectId().toString());
                                details.setProjectName(project.getNamaProject());
                                details.setProjectAddress(project.getLokasi());
                                details.setJrkMax(project.getJrkMax());
                                details.setGpsLongitude(project.getGpsLongitude());
                                details.setGpsLatitude(project.getGpsLatitude());
                                details.setJamMasuk(project.getJamMasuk());
                                details.setJamKeluar(project.getJamKeluar());
        
                                if (penempatan != null && "0".equals(penempatan.getActive())) {
                                    details.setActive(penempatan.getActive());
                                } else {
                                    details.setActive(null);
                                }
        
                                projectDetailsList.add(details);
                            }
                        }
        
                        if (!projectDetailsList.isEmpty()) {
                            response.put("success", true);
                            response.put("message", "Project details retrieved successfully");
                            response.put("data", projectDetailsList);
                            return ResponseEntity.status(HttpStatus.OK).body(response);
                        } else {
                            response.put("success", false);
                            response.put("message", "No member data found!");
                            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                        }
        
                    } else {
                        response.put("success", false);
                        response.put("message", "No project placement found for the user!");
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                    }
                } else {
                    // Handle the case where the token format is invalid
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", false);
                    response.put("message", "Invalid token format");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                }
            } catch (Exception e) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Failed to retrieve project details");
                response.put("error", e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        }
        
    }

        // public ResponseEntity<Map<String, Object>> getAllAbsen(@RequestHeader("Authorization") String tokenWithBearer, @RequestBody MemberRequest request) {
        //         try {
        //             if (tokenWithBearer.startsWith("Bearer ")) {
        //                 List<AbsenEntity> absenEntities = absenRepository.findAll();
        //                 AbsenResponse absenResponse = 
        //             } else {
        //                 // Handle the case where the token is not valid
        //                 Map<String, Object> response = new HashMap<>();
        //                 response.put("success", false);
        //                 response.put("message", "Invalid token format");
                        
        //                 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        //             }
        //         } catch (Exception e) {
        //             Map<String, Object> response = new HashMap<>();
        //             response.put("success", false);
        //             response.put("message", "Failed to retrieve Absen data");
        //             response.put("error", e.getMessage());

        //             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        //         }
        //     }

