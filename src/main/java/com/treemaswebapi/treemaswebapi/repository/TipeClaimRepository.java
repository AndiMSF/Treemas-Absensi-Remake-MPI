package com.treemaswebapi.treemaswebapi.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.treemaswebapi.treemaswebapi.entity.ClaimEntity.TipeClaimEntity;

@Repository
public interface TipeClaimRepository extends JpaRepository<TipeClaimEntity, Long> {
}