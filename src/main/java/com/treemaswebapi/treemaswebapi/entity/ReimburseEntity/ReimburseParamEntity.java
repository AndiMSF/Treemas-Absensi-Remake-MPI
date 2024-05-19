package com.treemaswebapi.treemaswebapi.entity.ReimburseEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "tbl_reimburse_param", schema = "public")
public class ReimburseParamEntity {

    @Id
    @Column(name = "reimburse_id")
    private String reimburseId;

    @Column(name = "nama")
    private String nama;

    @Column(name = "nominal")
    private BigDecimal nominal;

    @Column(name = "note")
    private String note;

    @Column(name = "is_leader")
    private String isLeader;
}
