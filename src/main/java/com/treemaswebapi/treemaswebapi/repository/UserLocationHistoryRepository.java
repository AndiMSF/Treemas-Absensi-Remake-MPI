package com.treemaswebapi.treemaswebapi.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.treemaswebapi.treemaswebapi.entity.UserLocationHistoryEntity.UserLocationHistoryEntity;

@Repository
public interface UserLocationHistoryRepository extends JpaRepository<UserLocationHistoryEntity, Long> {
    List<UserLocationHistoryEntity> findByUserIdAndDtmcrtBetween(String userId, Timestamp startDate, Timestamp endDate);
}