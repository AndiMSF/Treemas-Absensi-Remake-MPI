package com.treemaswebapi.treemaswebapi.entity.AbsenEntity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

    @AllArgsConstructor
    @Data
    @Builder
    @RequiredArgsConstructor
    public class AbsenResponse {
        private String idAbsen;
        private String projectId;
        private String projectAdress; 
        private String nik; 
        private String nama;
        private String tglAbsen;


        // Getters and setters
    }