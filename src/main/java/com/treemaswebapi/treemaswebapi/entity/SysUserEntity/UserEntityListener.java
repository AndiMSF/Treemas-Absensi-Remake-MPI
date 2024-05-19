package com.treemaswebapi.treemaswebapi.entity.SysUserEntity;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

public class UserEntityListener {

    @PrePersist
    public void onPrePersist(Object entity) {
        if (entity instanceof SysUserEntity) {
            SysUserEntity sysUserEntity = (SysUserEntity) entity;
            // Logic to be executed before the entity is persisted (created)
            if (sysUserEntity.getPassword() == null) {
                sysUserEntity.setSqlPassword("123456"); // Set default password if not set
            }
        }
    }

    @PreUpdate
    public void onPreUpdate(Object entity) {
        if (entity instanceof SysUserEntity) {
            SysUserEntity sysUserEntity = (SysUserEntity) entity;
            // Logic to be executed before the entity is updated
            if (sysUserEntity.getPassword() == null) {
                sysUserEntity.setSqlPassword("123456"); // Set default password if not set
            }
        }
    }
}
