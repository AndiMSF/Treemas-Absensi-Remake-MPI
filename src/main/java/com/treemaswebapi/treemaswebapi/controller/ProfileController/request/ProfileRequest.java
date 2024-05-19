package com.treemaswebapi.treemaswebapi.controller.ProfileController.request;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import com.treemaswebapi.treemaswebapi.entity.UserRole.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileRequest {
    private String nik;
    private String nama;
    private String tempatLahir;
    private Date tanggalLahir;
    private String jenisKelamin;
    private String agama;
    private String kewarganegaraan;
    private String kodePos;
    private String alamatKtp;
    private String noHp;
    private String email;
    private String noRek;
    private String noNpwp;
    private String jenjangPendidikan;
    private Date tanggalBergabung;
    private String alamatSekarang;
    private String statusPerkawinan;
    private String golonganDarah;
    private String nomorKtp;
    private String emergencyContact;
    private String statusEmergency;
    private String alamatEmergency;
    private String telpEmergency;
    private String projectId;
    private String divisi;
    private String nikLeader;
    private String isLeader;
    private String usrUpd;
    private Timestamp dtmUpd;
    private String handsetImei;
    private BigDecimal hakCuti;
    private String isKaryawan;
    private String fullName;

    // image file
    private String foto;
    private String fotoKtp;
    private String fotoNpwp;
    private String fotoKk;
    private String fotoAsuransi;

    // image path
    private String fotoPath;
    private String fotoKtpPath;
    private String fotoNpwpPath;
    private String fotoKkPath;
    private String fotoAsuransiPath;

    // different table
    private Role role;
    private String sql_password;
    private String isLogin;

    // new password
    private String oldPassword;
    private String newPassword;
    private String confPassword;

    private String selectedRole;
    private String selectedProject;
}
