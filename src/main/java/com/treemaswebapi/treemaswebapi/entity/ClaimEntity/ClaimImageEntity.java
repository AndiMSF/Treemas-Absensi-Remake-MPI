package com.treemaswebapi.treemaswebapi.entity.ClaimEntity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;

// id_claim FK dari tbl_tipe_claim

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_claim_image", schema = "public")
public class ClaimImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nik")
    private String nik;

    @Column(name = "tanggal")
    private LocalDate tanggal;

    @Column(name = "id_claim")
    private String idClaim;

    @Column(name = "image_64")
    private String image64;

    @Column(name = "usrcrt")
    private String usrCrt;

    @Column(name = "dtmcrt")
    private Timestamp dtmCrt;
}