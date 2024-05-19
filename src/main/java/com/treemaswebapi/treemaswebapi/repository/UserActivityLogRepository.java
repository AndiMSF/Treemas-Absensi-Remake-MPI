package com.treemaswebapi.treemaswebapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.treemaswebapi.treemaswebapi.entity.UserActivityLogEntity.UserActivityLogEntity;

@Repository
public interface UserActivityLogRepository extends JpaRepository<UserActivityLogEntity, Long> {
}