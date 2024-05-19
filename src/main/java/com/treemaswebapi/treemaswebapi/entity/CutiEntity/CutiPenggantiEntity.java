package com.treemaswebapi.treemaswebapi.entity.CutiEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor 
@Entity
@Table(name = "tbl_cuti_pengganti", schema = "public")
public class CutiPenggantiEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nik")
    private String nik;

    @Column(name = "tgl_dapat")
    private Date tglDapat;

    @Column(name = "jml_cuti")
    private BigDecimal jmlCuti;

    @Column(name = "note_cuti_pengganti")
    private String noteCutiPengganti;

    @Column(name = "usrcrt")
    private String usrCrt;

    @Column(name = "dtmcrt")
    private Timestamp dtmCrt;

    @Column(name = "is_exp")
    private String isExp;
}