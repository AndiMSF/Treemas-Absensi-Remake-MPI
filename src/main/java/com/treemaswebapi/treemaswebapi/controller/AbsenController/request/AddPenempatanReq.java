package com.treemaswebapi.treemaswebapi.controller.AbsenController.request;

import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class AddPenempatanReq {
    private String projectId;
    private String isActive;
}
