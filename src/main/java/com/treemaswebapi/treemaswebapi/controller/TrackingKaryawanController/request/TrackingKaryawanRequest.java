package com.treemaswebapi.treemaswebapi.controller.TrackingKaryawanController.request;
import java.sql.Timestamp;
import java.time.LocalDate;

import com.treemaswebapi.treemaswebapi.entity.ProjectEntity.ProjectEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrackingKaryawanRequest {
    //yang diminta
    private String userId;
    private Double latitude;
    private Double longitude;
    private Long accuracy;
    private Timestamp dtmcrt;
}
