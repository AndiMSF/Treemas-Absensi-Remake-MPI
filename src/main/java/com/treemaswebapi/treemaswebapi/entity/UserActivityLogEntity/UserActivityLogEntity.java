package com.treemaswebapi.treemaswebapi.entity.UserActivityLogEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

// branch_id FK dari sys_userbranch

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_activity_log", schema = "public")
public class UserActivityLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "activity_seqno")
    private Long activitySeqno;

    @Column(name = "userid")
    private String userid;

    @Column(name = "branch_id")
    private String branchId;

    @Column(name = "activity")
    private String activity;

    @Column(name = "dtmcrt")
    private Timestamp dtmCrt;
}