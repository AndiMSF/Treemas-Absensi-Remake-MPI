package com.treemaswebapi.treemaswebapi.controller.Management.User.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ManagementUserRequest {
    private String id;
    private String nik;
}
