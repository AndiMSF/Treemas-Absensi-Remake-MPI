package com.treemaswebapi.treemaswebapi.controller.AuthController;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class  LoginRequest {
    //yang diminta
    private String nik;
    private String password;
    private String handsetImei;
    private String isWebAccess;
}
