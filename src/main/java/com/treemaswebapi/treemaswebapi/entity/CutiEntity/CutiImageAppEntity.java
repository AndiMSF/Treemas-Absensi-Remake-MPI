package com.treemaswebapi.treemaswebapi.entity.CutiEntity;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tbl_cuti_image_app", schema = "public")
public class CutiImageAppEntity {
    @Id
    private Long id;

    @Column(name = "nik")
    private String nik;

    @Column(name = "tgl_mulai")
    private LocalDate tglMulai;

    @Column(name = "flg_ket")
    private String flgKet;

    @Column(name = "image")
    private String image;

    @Column(name = "usrcrt")
    private String usrCrt;

    @Column(name = "dtmcrt")
    private Timestamp dtmCrt;

    @Column(name = "usrapp")
    private String usrApp;

    @Column(name = "dtmapp")
    private Timestamp dtmapp;
    
    @Column(name = "is_approve")
    private String isApprove;
}
