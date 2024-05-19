package com.treemaswebapi.treemaswebapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.treemaswebapi.treemaswebapi.entity.SysUserEntity.SysUserMemberEntity;

@Repository
public interface SysUserMemberRepository extends JpaRepository<SysUserMemberEntity, Long> {
    List<SysUserMemberEntity> findByNikLeader(String nikLeader);
    void deleteByNikUserAndNikLeader(String nikUser, String nikLeader);
    // Tambahkan metode untuk memeriksa apakah data dengan nikUser dan nikLeader ada
    boolean existsByNikUserAndNikLeader(String nikUser, String nikLeader);
    @Query("SELECT nikUser FROM SysUserMemberEntity WHERE nikLeader = :nikLeader")
    List<String> findAllByNikLeader(String nikLeader);
}