package com.treemaswebapi.treemaswebapi.controller.Parameter.General.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GeneralAddRequest {
    
    private String id;
    private String dataType;
    private String val;
    private String paramDesc;
    private String isVisible;
}
