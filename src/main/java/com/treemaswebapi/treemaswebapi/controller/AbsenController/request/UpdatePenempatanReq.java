package com.treemaswebapi.treemaswebapi.controller.AbsenController.request;

import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class UpdatePenempatanReq {
    private List<ProjectEntities> projectTerpilih;

    
    public static class ProjectEntities{
        private String active;
        private String projectId;
        public String getActive() {
            return active;
        }
        public void setActive(String active) {
            this.active = active;
        }
        public String getProjectId() {
            return projectId;
        }
        public void setProjectId(String projectId) {
            this.projectId = projectId;
        }
    }

}
