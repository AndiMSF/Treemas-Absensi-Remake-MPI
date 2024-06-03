package com.treemaswebapi.treemaswebapi.entity.ClaimEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;

// id_claim FK dari tbl_tipe_claim

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_claim", schema = "public")
public class ClaimEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nik")
    private String nik;

    @Column(name = "nama")
    private String nama;

    @Column(name = "hari")
    private String hari;

    @Column(name = "tanggal")
    private LocalDate tanggal;

    @Column(name = "nominal")
    private BigDecimal nominal;

    @Column(name = "keterangan")
    private String keterangan;

    @Column(name = "usrcrt")
    private String usrCrt;
    
    @Column(name = "dtmcrt")
    private Timestamp dtmCrt;

    @OneToOne
    @JoinColumn(name = "id_claim", referencedColumnName = "nama_claim")
    private TipeClaimEntity tipeClaimEntity;

    @Transient
    private Boolean gambarnya;
}