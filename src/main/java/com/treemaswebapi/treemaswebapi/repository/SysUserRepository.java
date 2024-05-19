package com.treemaswebapi.treemaswebapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.treemaswebapi.treemaswebapi.entity.JabatanEntity.JabatanEntity;
import com.treemaswebapi.treemaswebapi.entity.SysUserEntity.SysUserEntity;

@Repository
public interface SysUserRepository extends JpaRepository<SysUserEntity, String> {
    Optional<SysUserEntity> findByUserId(String userid);
    Optional<SysUserEntity> findByEmail(String email);
    List<SysUserEntity> findAllByUserIdNot(String userId);
    // Find all users with role "LEAD" or "HEAD"
    List<SysUserEntity> findAllByRole_JabatanIdIn(List<String> jabatanIds);

    @Query("SELECT u FROM SysUserEntity u WHERE u.role.jabatanId = :jabatanId")
    List<SysUserEntity> findByRole(@Param("jabatanId") String jabatanId);
    @Query("SELECT fullName FROM SysUserEntity WHERE userId = :userId")
    String findNameByNik(@Param("userId") String userId);
}
