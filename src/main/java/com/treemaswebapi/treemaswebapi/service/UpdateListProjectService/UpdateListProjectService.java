package com.treemaswebapi.treemaswebapi.service.UpdateListProjectService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
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

import com.treemaswebapi.treemaswebapi.repository.PenempatanRepository;
import com.treemaswebapi.treemaswebapi.repository.ProjectRepository;
import com.treemaswebapi.treemaswebapi.repository.KaryawanRepository;
import com.treemaswebapi.treemaswebapi.entity.PenempatanEntity.PenempatanEntity;
import com.treemaswebapi.treemaswebapi.entity.ProjectEntity.ProjectEntity;
import com.treemaswebapi.treemaswebapi.config.JwtService;
import com.treemaswebapi.treemaswebapi.controller.AbsenController.request.AddPenempatanReq;
import com.treemaswebapi.treemaswebapi.controller.AbsenController.request.UpdatePenempatanReq;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpdateListProjectService {
    private final JwtService jwtService;
    private final ProjectRepository projectRepository;
    private final PenempatanRepository penempatanRepository;
    private final KaryawanRepository karyawanRepository;

    public ResponseEntity<Map<String, Object>> listProject(@RequestHeader String tokenWithBearer) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (tokenWithBearer.startsWith("Bearer ")) {
                String token = tokenWithBearer.substring("Bearer ".length());
    
                // Ekstrak username dari token
                String nik = jwtService.extractUsername(token);
    
                // Ambil semua data dari tabel project
                List<ProjectEntity> allProjects = projectRepository.findAll();
    
                // Ambil semua data dari tabel penempatan yang aktif
                List<PenempatanEntity> activePenempatanList = penempatanRepository.findAllByNikAndActive(nik, "1");
    
                // Buat peta untuk penempatan aktif berdasarkan projectId
                Map<String, PenempatanEntity> activePenempatanMap = new HashMap<>();
                for (PenempatanEntity penempatan : activePenempatanList) {
                    activePenempatanMap.put(penempatan.getProjectId().getProjectId(), penempatan);
                }
    
                // Buat daftar akhir proyek yang akan dikembalikan
                List<Map<String, Object>> finalProjects = new ArrayList<>();
    
                // Tambahkan semua proyek dari tabel project, dan jika ada penempatan aktif, gantikan datanya
                for (ProjectEntity project : allProjects) {
                    if (activePenempatanMap.containsKey(project.getProjectId())) {
                        // Jika ada penempatan aktif untuk proyek ini, gunakan data dari penempatan
                        PenempatanEntity penempatan = activePenempatanMap.get(project.getProjectId());
                        Map<String, Object> projectData = new HashMap<>();
                        projectData.put("projectId", penempatan.getProjectId().getProjectId());
                        projectData.put("isActive", penempatan.getActive());
                        projectData.put("namaProject", penempatan.getProjectId().getNamaProject());
                        projectData.put("lokasi", penempatan.getProjectId().getLokasi());
                        projectData.put("no_tlpn", penempatan.getProjectId().getNoTlpn());
                        projectData.put("gps_latitude", penempatan.getProjectId().getGpsLatitude());
                        projectData.put("gps_longitude", penempatan.getProjectId().getGpsLongitude());
                        finalProjects.add(projectData);
                    } else {
                        // Jika tidak ada penempatan aktif, gunakan data dari project
                        Map<String, Object> projectData = new HashMap<>();
                        projectData.put("projectId", project.getProjectId());
                        projectData.put("isActive", "0");
                        projectData.put("namaProject", project.getNamaProject());
                        projectData.put("lokasi", project.getLokasi());
                        projectData.put("no_tlpn", project.getNoTlpn());
                        projectData.put("gps_latitude", project.getGpsLatitude());
                        projectData.put("gps_longitude", project.getGpsLongitude());
                        finalProjects.add(projectData);
                    }
                }
    
                response.put("success", true);
                response.put("data", finalProjects);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Invalid token format");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to update list of projects");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
        }
    }
    

//updateProject ini fungsinya untuk masukin project yang udah dipilih sama user, dan save ke penempatanEntity
public ResponseEntity<Map<String, Object>> updateProject(@RequestHeader String tokenWithBearer, @RequestBody AddPenempatanReq addPenempatanReq) {
        try {
            if (tokenWithBearer.startsWith("Bearer ")) {
                String token = tokenWithBearer.substring("Bearer ".length());
                String nik = jwtService.extractUsername(token);

                // Initialize the response map outside the loop
                Map<String, Object> response = new HashMap<>();

                // Fetch the ProjectEntity using the project ID from the request
                ProjectEntity projectEntity = projectRepository.findById(addPenempatanReq.getProjectId())
                        .orElseThrow(() -> new RuntimeException("Project not found"));

                
                // Langsung set ke table penempatan
                // Check if PenempatanEntity already exists
                PenempatanEntity penempatanEntity = penempatanRepository.findByNikAndProjectId(nik, projectEntity)
                        .orElse(null);

                if (penempatanEntity == null) {
                    // Create a new PenempatanEntity if it does not exist
                    penempatanEntity = PenempatanEntity.builder()
                        .nik(nik)
                        .projectId(projectEntity)
                        .active(addPenempatanReq.getIsActive())
                        .build();
                } else {
                    // Update the isActive field if PenempatanEntity already exists
                    penempatanEntity.setActive(addPenempatanReq.getIsActive());
                }

                // Save the PenempatanEntity to the repository
                penempatanRepository.save(penempatanEntity);
                // Set the response message based on the isActive value
                if ("0".equals(addPenempatanReq.getIsActive())) {
                    response.put("message", "Project removed successfully");
                } else {
                    response.put("message", "Project added successfully");
                }
                response.put("success", true);
                return ResponseEntity.ok(response);

            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "TOKEN ANDA TIDAK VALID");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }
        } catch (Exception e) {
            // Handle exceptions
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to add list of projects");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}