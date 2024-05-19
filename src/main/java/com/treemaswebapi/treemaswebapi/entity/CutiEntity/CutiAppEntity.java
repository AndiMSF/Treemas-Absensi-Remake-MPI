package com.treemaswebapi.treemaswebapi.entity.CutiEntity;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_cuti_app", schema = "public")
public class CutiAppEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nik")
    private String nik;

    @Column(name = "nama")
    private String nama;

    @Column(name = "tgl_mulai")
    private LocalDate tglMulai;

    @Column(name = "tgl_selesai")
    private LocalDate tglSelesai;

    @Column(name = "tgl_kembali_kerja")
    private LocalDate tglKembaliKerja;

    @Column(name = "keperluan_cuti")
    private String keperluanCuti;

    @Column(name = "alamat_cuti")
    private String alamatCuti;

    @Column(name = "flg_ket")
    private String flgKet;

    @Column(name = "usrcrt")
    private String usrCrt;

    @Column(name = "dtmcrt")
    private Timestamp dtmCrt;

    @Column(name = "usrapp")
    private String usrApp;

    @Column(name = "dtmapp")
    private Timestamp dtmApp;

    @Column(name = "flag_app")
    private String flagApp;

    @Column(name = "note_app")
    private String noteApp;

    @Column(name = "is_approved")
    private String isApproved;

    @Column(name = "jml_cuti_bersama")
    private BigDecimal jmlCutiBersama;

    @Column(name = "jml_cuti_khusus")
    private BigDecimal jmlCutiKhusus;

    @Column(name = "sisa_cuti")
    private BigDecimal sisaCuti;

    @Column(name = "jml_cuti")
    private BigDecimal jmlCuti;

    @OneToOne
    @JoinColumn(name = "jenis_cuti", referencedColumnName = "id")
    private MasterCutiEntity jenisCuti;

    @Transient
    private Boolean gambarnya;
}
