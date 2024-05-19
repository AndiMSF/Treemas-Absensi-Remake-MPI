package com.treemaswebapi.treemaswebapi.config;

import java.io.IOException;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.treemaswebapi.treemaswebapi.entity.SysUserEntity.SysUserEntity;
import com.treemaswebapi.treemaswebapi.repository.SysUserRepository;

import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AccountLockFilter extends OncePerRequestFilter {
    private final SysUserRepository sysUserRepository;

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String nik = extractUserEmail(request);

        if (nik != null) {
            Optional<SysUserEntity> sysUser = sysUserRepository.findByUserId(nik);
            if (sysUser != null && sysUser.get().getIsLocked() == "1") {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("Account is locked. Please contact support.");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
    private String extractUserEmail(HttpServletRequest request){
        return null;
    }
    
}
