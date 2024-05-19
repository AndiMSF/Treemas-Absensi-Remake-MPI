package com.treemaswebapi.treemaswebapi.controller.AbsenController;

import com.treemaswebapi.treemaswebapi.entity.AbsenEntity.AbsenTrackingData.AbsenTrackingData;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AbsenResponse {
    AbsenTrackingData absenTrackingData;
    String absenImg;
    String pp;
    // buat response unprocessedAbsenEntity
    
}
