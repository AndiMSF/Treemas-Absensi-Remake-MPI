package com.treemaswebapi.treemaswebapi.controller.MemberController.request;

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
public class MemberRequest {
    //yang diminta
    private ProjectEntity projectId;
    private LocalDate targetDate;
    private String handsetImei;
    private String isWebAccess;
}
