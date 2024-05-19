package com.treemaswebapi.treemaswebapi.entity.KaryawanEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_karyawan_image", schema = "public")
public class KaryawanImageEntity {

    @Id
    @Column(name = "nik")
    private String nik;

    @Column(name = "foto_ktp")
    private String fotoKtp;

    @Column(name = "foto_ktp_path")
    private String fotoKtpPath;

    @Column(name = "foto_npwp")
    private String fotoNpwp;

    @Column(name = "foto_npwp_path")
    private String fotoNpwpPath;

    @Column(name = "foto_kk")
    private String fotoKk;

    @Column(name = "foto_kk_path")
    private String fotoKkPath;

    @Column(name = "foto_asuransi")
    private String fotoAsuransi;

    @Column(name = "foto_asuransi_path")
    private String fotoAsuransiPath;

    @Column(name = "foto_path")
    private String fotoPath;

    @Column(name = "foto")
    private String foto;

    // @OneToOne
    // @MapsId
    // @JoinColumn(name = "nik")
    // private KaryawanEntity karyawan2;
}
