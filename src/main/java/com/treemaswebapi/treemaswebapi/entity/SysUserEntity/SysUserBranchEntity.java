package com.treemaswebapi.treemaswebapi.entity.SysUserEntity;


import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// userId FK dari sys_user

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sys_userbranch", schema = "public")
@IdClass(SysUserBranchEntityPK.class)
public class SysUserBranchEntity {
    @Id
    @Column(name = "branchid")
    private String branchId;

    @Id
    @Column(name = "userid")
    private String userId;

    @Column(name = "usrupd")
    private String userUpd;

    @Column(name = "dtmupd")
    private Timestamp dtmUpd;

    @ManyToOne
    @JoinColumn(name = "userid", referencedColumnName = "userid", insertable = false, updatable = false)
    private SysUserEntity sysUser;
}
