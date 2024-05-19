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
import com.treemaswebapi.treemaswebapi.controller.AbsenController.request.UpdatePenempatanReq;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpdateListProjectService {
    private final JwtService jwtService;
    private final ProjectRepository projectRepository;
    private final PenempatanRepository penempatanRepository;
    private final KaryawanRepository karyawanRepository;

public ResponseEntity<Map<String, Object>> listProject(@RequestHeader String tokenWithBearer, @RequestBody String projectId){
    Map<String, Object> response = new HashMap<>();
    try{
        if (tokenWithBearer.startsWith("Bearer ")) {
                String token = tokenWithBearer.substring("Bearer ".length());
                String nik = jwtService.extractUsername(token);
                List<ProjectEntity> allProjects = projectRepository.findAll();
                Optional<PenempatanEntity> penempatanOptional = penempatanRepository.findByNikAndActive(nik,"1");

                if (penempatanOptional.isPresent()){
                    ProjectEntity activeProjectId = penempatanOptional.get().getProjectId();
                    response.put("success", true);
                    response.put("message", "Projects retrieved successfully");
                    response.put("activeProjectId", activeProjectId);
                    response.put("success", allProjects);
                    return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
                    }else{
                    response.put("success", true);
                    response.put("message", "Projects retrieved successfully");
                    response.put("activeProjectId", "active project belom ada");
                    response.put("projects", allProjects);
                    return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
                    }
                }else{
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
public ResponseEntity<Map<String, Object>> updateProject(@RequestHeader String tokenWithBearer, @RequestBody UpdatePenempatanReq updatePenempatanReq) {
        try {
            if (tokenWithBearer.startsWith("Bearer ")) {
                String token = tokenWithBearer.substring("Bearer ".length());
                String nik = jwtService.extractUsername(token);
                String nama = karyawanRepository.findNamaByNik(nik);

                // Initialize the response map outside the loop
                Map<String, Object> response = new HashMap<>();
                boolean successFlag = false;
                List<PenempatanEntity> updatedPenempatanEntities = new ArrayList<>();

                List<UpdatePenempatanReq.ProjectEntities> projectEntities = updatePenempatanReq.getProjectTerpilih();
                if (projectEntities != null) {
                    for (UpdatePenempatanReq.ProjectEntities projectEntity : projectEntities) {
                        String active = projectEntity.getActive();
                        String projectId = projectEntity.getProjectId();
                        ProjectEntity existingProject = projectRepository.findByProjectId(projectId);
                        PenempatanEntity existingPenempatan = penempatanRepository.findActiveByProjectIdAndNik(existingProject, nik);

                        if (existingPenempatan != null) {
                            PenempatanEntity penempatanEntity = existingPenempatan;
                            // Set values in penempatanEntity
                            penempatanEntity.setActive(active);
                            penempatanEntity.setNik(nik);
                            penempatanEntity.setUsrUpd(nama);
                            penempatanEntity.setDtmUpd(Timestamp.valueOf(LocalDateTime.now()));

                            // Save penempatanEntity
                            penempatanRepository.save(penempatanEntity);

                            // Set success flag to true
                            successFlag = true;
                            // Add updated penempatanEntity to the list
                            updatedPenempatanEntities.add(penempatanEntity);

                        } else {
                            PenempatanEntity penempatanEntity = new PenempatanEntity();

                            penempatanEntity.setActive(active);
                            penempatanEntity.setDtmUpd(Timestamp.valueOf(LocalDateTime.now()));
                            penempatanEntity.setNik(nik);
                            penempatanEntity.setProjectId(existingProject);
                            penempatanEntity.setUsrUpd(nama);

                            penempatanRepository.save(penempatanEntity);

                            // Set success flag to true
                            successFlag = true;
                            // Add created penempatanEntity to the list
                            updatedPenempatanEntities.add(penempatanEntity);
                        }
                    }

                    // Build and return the response after the loop has completed
                    if (successFlag) {
                        response.put("success", true);
                        response.put("message", "Berhasil update projects");
                        response.put("data", updatedPenempatanEntities);
                    } else {
                        response.put("success", false);
                        response.put("message", "No updates performed");
                    }
                    return ResponseEntity.ok(response);
                } else {
                    // Set response values for the case where PROJECT LIST IS NULL
                    response.put("success", false);
                    response.put("message", "PROJECT LIST IS NULL");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                }
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
            response.put("message", "Failed to update list of projects");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}