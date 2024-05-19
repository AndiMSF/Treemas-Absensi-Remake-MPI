package com.treemaswebapi.treemaswebapi.service.MasterData.Project;

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
import com.treemaswebapi.treemaswebapi.controller.MasterData.Project.request.ProjectRequest;
import com.treemaswebapi.treemaswebapi.entity.KaryawanEntity.KaryawanEntity;
import com.treemaswebapi.treemaswebapi.entity.ProjectEntity.ProjectEntity;
import com.treemaswebapi.treemaswebapi.repository.KaryawanRepository;
import com.treemaswebapi.treemaswebapi.repository.ProjectRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final JwtService jwtService;
    private final KaryawanRepository karyawanRepository;

    public ResponseEntity<Map<String, Object>> projectAdd(
        ProjectRequest request
    ) {
        try {

            // Check if individual fields are null or empty
            if (request.getProjectId() == null || request.getProjectId().isEmpty()) {
                throw new RuntimeException("Project ID cannot be null or empty");
            }

            if (request.getNamaProject() == null || request.getNamaProject().isEmpty()) {
                throw new RuntimeException("Nama Project cannot be null or empty");
            }

            if (request.getLokasi() == null || request.getLokasi().isEmpty()) {
                throw new RuntimeException("Lokasi cannot be null or empty");
            }

            if (request.getGpsLatitude() == null) {
                throw new RuntimeException("Latitude cannot be null");
            }

            if (request.getGpsLongitude() == null) {
                throw new RuntimeException("Longitude cannot be null or empty");
            }

            if (request.getBiayaReimburse() == null) {
                throw new RuntimeException("Biaya Reimburse cannot be null or empty");
            }
            // Check if project with the given projectId already exists
            if (projectRepository.existsById(request.getProjectId())) {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "Failed");
                response.put("message", "Project with the same ID already exists");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            var projectEntity = ProjectEntity.builder()
                .projectId(request.getProjectId())
                .namaProject(request.getNamaProject())
                .noTlpn(request.getNoTlpn())
                .kota(request.getKota())
                .lokasi(request.getLokasi())
                .gpsLatitude(request.getGpsLatitude())
                .gpsLongitude(request.getGpsLongitude())
                .biayaReimburse(request.getBiayaReimburse())
                .jrkMax(request.getJrkMax())
                .jamKerja(request.getJamKerja())
                .jamMasuk(request.getJamMasuk())
                .jamKeluar(request.getJamKeluar())
            .build();

            projectRepository.save(projectEntity);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "Success");
            response.put("message", "Project Created");
            response.put("data", projectEntity);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "Failed");
            response.put("message", "Failed to Create Project!");
            response.put("error", e.getMessage());
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    public ResponseEntity<Map<String, Object>> projectGet() {
        try {
            List<ProjectEntity> project = projectRepository.findAll();

            Map<String, Object> response = new HashMap<>();
            response.put("status", "Success");
            response.put("message", "Retrieved");
            response.put("data", project);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "Failed");
            response.put("message", "Failed to retrieve project");
            response.put("error", e.getMessage());
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    public ResponseEntity<Map<String, Object>> projectUpdate(
        ProjectRequest request,
        String id,
        @RequestHeader("Authorization") String jwtToken
    ) {
        try {

            // Cari siapa yang akses api ini
            String token = jwtToken.substring(7);
            String userToken = jwtService.extractUsername(token);
            
            Optional<KaryawanEntity> user = karyawanRepository.findByNik(userToken);
            String nama = user.get().getNama();
            

            Optional<ProjectEntity> projectOptional = projectRepository.findById(id);
            long currentTimeMillis = System.currentTimeMillis();
            Timestamp dtmUpd = new Timestamp(currentTimeMillis - (currentTimeMillis % 1000));
            // Check if individual fields are null or empty
            if (request.getProjectId() == null || request.getProjectId().isEmpty()) {
                throw new RuntimeException("Project ID cannot be null or empty");
            }

            if (request.getNamaProject() == null || request.getNamaProject().isEmpty()) {
                throw new RuntimeException("Nama Project cannot be null or empty");
            }

            if (request.getLokasi() == null || request.getLokasi().isEmpty()) {
                throw new RuntimeException("Lokasi cannot be null or empty");
            }

            if (request.getGpsLatitude() == null) {
                throw new RuntimeException("Latitude cannot be null");
            }

            if (request.getGpsLongitude() == null) {
                throw new RuntimeException("Longitude cannot be null or empty");
            }

            if (request.getBiayaReimburse() == null) {
                throw new RuntimeException("Biaya Reimburse cannot be null or empty");
            }


            if (projectOptional.isPresent()) {
                ProjectEntity newProject = projectOptional.get();

                newProject.setNamaProject(request.getNamaProject());
                newProject.setNoTlpn(request.getNoTlpn());
                newProject.setKota(request.getKota());
                newProject.setLokasi(request.getLokasi());
                newProject.setGpsLatitude(request.getGpsLatitude());
                newProject.setGpsLongitude(request.getGpsLongitude());
                newProject.setBiayaReimburse(request.getBiayaReimburse());
                newProject.setJrkMax(request.getJrkMax());
                newProject.setJamKerja(request.getJamKerja());
                newProject.setJamMasuk(request.getJamMasuk());
                newProject.setJamKeluar(request.getJamKeluar());
                newProject.setUsrupd(nama);
                newProject.setDtmupd(dtmUpd);

                projectRepository.save(newProject);
                
                Map<String, Object> response = new HashMap<>();
                response.put("status", "Success");
                response.put("message", "Project Updated");
                response.put("data", newProject);

                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "Failed");
                response.put("message", "Project Not Found");

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "Failed");
            response.put("message", "Failed To Create Project");
            response.put("error", e.getMessage());
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    public ResponseEntity<Map<String, String>> projectDelete(
        String id
    ) {
        try {
            // Cari Announcement berdasarkan ID
            Optional<ProjectEntity> projectOptional = projectRepository.findById(id);
            if (projectOptional.isPresent()) {
                projectRepository.deleteById(id);

                Map<String, String> response = new HashMap<>();
                response.put("status", "Success");
                response.put("message", "Project deleted");
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                Map<String, String> response = new HashMap<>();
                response.put("status", "Failed");
                response.put("message", "Project not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "Failed");
            response.put("message", "Failed To Delete Project");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
