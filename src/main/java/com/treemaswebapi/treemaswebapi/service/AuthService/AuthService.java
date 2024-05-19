package com.treemaswebapi.treemaswebapi.service.AuthService;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import com.treemaswebapi.treemaswebapi.config.JwtService;
import com.treemaswebapi.treemaswebapi.controller.AuthController.ChangePasswordRequest;
import com.treemaswebapi.treemaswebapi.controller.AuthController.ForgotPasswordRequest;
import com.treemaswebapi.treemaswebapi.controller.AuthController.LoginRequest;
import com.treemaswebapi.treemaswebapi.entity.KaryawanEntity.KaryawanEntity;
import com.treemaswebapi.treemaswebapi.entity.SysUserEntity.SysUserEntity;
import com.treemaswebapi.treemaswebapi.repository.KaryawanImageRepository;
import com.treemaswebapi.treemaswebapi.repository.KaryawanRepository;
import com.treemaswebapi.treemaswebapi.repository.SysUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lombok.RequiredArgsConstructor;

    @Service
    @RequiredArgsConstructor
    public class AuthService {
        

        private final SysUserRepository sysUserRepository;
        private final KaryawanRepository karyawanRepository;
        private final JwtService jwtService;
        private final AuthenticationManager authenticationManager;
        private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        private final KaryawanImageRepository karyawanImageRepository;

        public ResponseEntity<Map<String, Object>> login(LoginRequest request) {
            try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getNik(), request.getPassword())
            );
            var user = sysUserRepository.findByUserId(request.getNik())
                .orElseThrow();

            var userImg = karyawanImageRepository.findByNik(request.getNik());
            var karyawan = karyawanRepository.findByNik(request.getNik());
            
            // Check kalau times locked 1 langsung return
            if ("1".equals(user.getIsLocked())) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Account is Locked!");
                
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
     
            }
            
            String isWebAccess = request.getIsWebAccess();

            if ("0".equals(isWebAccess)) { // Check if it's not a web access

                Optional<KaryawanEntity> isHandsetImeiOptional = karyawanRepository.findByNik(request.getNik());
    
                if (isHandsetImeiOptional.isPresent()) {
                    KaryawanEntity karyawanEntity = isHandsetImeiOptional.get();
                    String existingHandsetImei = karyawanEntity.getHandsetImei();
                    String requestedHandsetImei = request.getHandsetImei();
    
                    if (existingHandsetImei == null) {
                        // If handsetImei in the database is null, set it from the request
                        karyawanEntity.setHandsetImei(requestedHandsetImei);
                        karyawanRepository.save(karyawanEntity);
                    } else if (!existingHandsetImei.equals(requestedHandsetImei)) {
                        // If handsetImei is already set and different from the request, send an unauthorized response
                        String message = "HANDSET IMEI MISMATCH!";
                        Map<String, Object> response = new HashMap<>();
                        response.put("success", false);
                        response.put("data", message);
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
                    }
                }
            }
            
            Optional<SysUserEntity> userOptional = sysUserRepository.findByUserId(request.getNik());
            SysUserEntity sysUser = userOptional.get();
            sysUser.setWrongPassCount((short) 0);
            sysUser.setActive("1");
            sysUser.setIsLogin("1");
            sysUserRepository.save(sysUser);
            System.out.println("Masuk Login");
            var jwtToken = jwtService.generateToken(user);
            
            Map<String, Object> userData = new HashMap<>();
            userData.put("nik", user.getUserId()); 
            userData.put("full_name", user.getFullName());
            userData.put("email", user.getEmail());
            userData.put("role", user.getRole().getJabatanId());
            userData.put("is_pass_chg", user.getIsPassChg());
            userData.put("karyawanImg", userImg.get().getFoto());
            userData.put("alamatKaryawan", karyawan.get().getAlamatSekarang());
            userData.put("jenisKelamin", karyawan.get().getJenisKelamin());
            

            Map<String, Object> data = new HashMap<>();
            data.put("user", userData);
            data.put("token", jwtToken);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Login successful");
            response.put("data", data);

            return ResponseEntity.status(HttpStatus.OK).body(response);

            } catch (BadCredentialsException e) {
                long currentTimeMillis = System.currentTimeMillis();
                Timestamp dtmCrt = new Timestamp(currentTimeMillis - (currentTimeMillis % 1000));
            // If there is a BadCredentialsException (wrong password), increment wrongPassCount
            SysUserEntity user = sysUserRepository.findByUserId(request.getNik())
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            // Increment wrongPassCount
            user.setWrongPassCount((short) (user.getWrongPassCount() + 1));
            user.setWrongPassTime(dtmCrt);
            // Check kalau times locked 1 langsung return
            if ("1".equals(user.getIsLocked())) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Account is Locked!");

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            
            }

            // Kalau sudah lebih dari 3 akan set is locked ke 1
            if (user.getWrongPassCount() >= 3) {
                user.setIsLocked("1");
                user.setTimesLocked(user.getTimesLocked() + 1);
                user.setLockedTime(dtmCrt);
            }
            // Save the updated user
            sysUserRepository.save(user);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Login failed!");
            response.put("error", e.getMessage());

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        }

        public ResponseEntity<Map<String, Object>> forgetPassword(
            ForgotPasswordRequest request) {
            try {            
                Optional<SysUserEntity> existingEmail = sysUserRepository.findByEmail(request.getEmail());
                // Check jika email dari token sama dengan email dari request maka akan do something...
                if (existingEmail.isPresent()) {
                    SysUserEntity user = existingEmail.get();
                    user.setSqlPassword(passwordEncoder.encode("123456"));
                    user.setIsPassChg("0");
                    sysUserRepository.save(user);
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("message", "Set password to default success");
                    response.put("data", user);

                    return ResponseEntity.status(HttpStatus.OK).body(response);
                } else {
                    // TODO: handle exception
                Map<String, Object> response = new HashMap<>();
                response.put("status", "error");
                response.put("message", "No Email found!");

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                }
            
            } catch (Exception e) {
                // TODO: handle exception
                Map<String, Object> response = new HashMap<>();
                response.put("status", "error");
                response.put("message", "Set Password to default failed!");
                response.put("error", e.getMessage());

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        }

        public ResponseEntity<Map<String, Object>> logout(String tokenWithBearer) {
            try {
                if (tokenWithBearer.startsWith("Bearer ")) {
                    String token = tokenWithBearer.substring("Bearer ".length());
                    String nik = jwtService.extractUsername(token);

                Optional<SysUserEntity> userOptional = sysUserRepository.findByUserId(nik);
                long currentTimeMillis = System.currentTimeMillis();
                Timestamp lastLogin = new Timestamp(currentTimeMillis - (currentTimeMillis % 1000));
                if (userOptional.isPresent()) {
                    SysUserEntity sysUser = userOptional.get();
                    sysUser.setIsLogin("0");
                    sysUser.setLastLogin(lastLogin);
                    sysUserRepository.save(sysUser);
                    
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("message", "Logout successful");
        
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                } else {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", false);
                    response.put("message", "User not found");
        
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                }
            }else{
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "INVALID TOKEN");

                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }
            } catch (Exception e) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Logout failed");
                response.put("error", e.getMessage());
        
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        }
        

        public ResponseEntity<Map<String, Object>> changePassword(
            @RequestHeader("Authorization") String jwtToken,
            ChangePasswordRequest request
            ) {
            try {          
                // Cari siapa yang akses api ini
                String token = jwtToken.substring(7);
                String nik = jwtService.extractUsername(token);  

                Optional<SysUserEntity> sysUser = sysUserRepository.findByUserId(nik);
                String nama = sysUser.get().getFullName();
                // Check jika email dari token sama dengan email dari request maka akan do something...
                if (sysUser.isPresent()) {
                    SysUserEntity user = sysUser.get();
                    String dbPassword = user.getPassword();
                    if (passwordEncoder.matches("123456", dbPassword)) {
                        if (request.getNewPassword() != null && request.getNewPassword().equals(request.getConfPassword())) {
                             user.setSqlPassword(passwordEncoder.encode(request.getConfPassword()));
                             user.setIsPassChg("1");
                             user.setLastPasschg(new Timestamp(System.currentTimeMillis()));
                             user.setDtmUpd(new Timestamp(System.currentTimeMillis()));
                             user.setUsrUpd(nama);
                                sysUserRepository.save(user);
                                Map<String, Object> response = new HashMap<>();
                                response.put("success", true);
                                response.put("message", "Password Changed");
                                response.put("data", user);

                                return ResponseEntity.status(HttpStatus.OK).body(response);
                        } else {
                                Map<String, Object> response = new HashMap<>();
                                response.put("success", false);
                                response.put("message", "New Password and Confirm Password Must Be Same!");

                                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                        }
                    } else {
                                Map<String, Object> response = new HashMap<>();
                                response.put("success", true);
                                response.put("message", "Password Isn't Default!");

                                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
                    }
                } else {
                    // TODO: handle exception
                Map<String, Object> response = new HashMap<>();
                response.put("status", "error");
                response.put("message", "No User found!");

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                }
            } catch (Exception e) {
                // TODO: handle exception
                Map<String, Object> response = new HashMap<>();
                response.put("status", "error");
                response.put("message", "Set Password to default failed!");
                response.put("error", e.getMessage());

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        }
    }