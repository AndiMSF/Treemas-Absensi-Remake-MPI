package com.treemaswebapi.treemaswebapi.controller.Management.UserAccess.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAccessRequest {
    private String jabatan;
    private String parent;
    private String child;
}
