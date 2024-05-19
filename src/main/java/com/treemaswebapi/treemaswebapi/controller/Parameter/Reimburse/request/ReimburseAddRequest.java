package com.treemaswebapi.treemaswebapi.controller.Parameter.Reimburse.request;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReimburseAddRequest {
    
    private String reimburseId;
    private String nama;
    private BigDecimal nominal;
    private String note;
}
