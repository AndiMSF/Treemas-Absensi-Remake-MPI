package com.treemaswebapi.treemaswebapi.entity.ClaimEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "tbl_tipe_claim", schema = "public")
public class TipeClaimEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_claim")
    private Long idClaim;

    @Column(name = "nama_claim")
    private String namaClaim;

    @Column(name = "value_claim")
    private BigDecimal valueClaim;

    @Column(name = "keterangan")
    private String keterangan;

    @Column(name = "usrcrt")
    private String usrCrt;

    @Column(name = "dtmcrt")
    private String dtmCrt;

    @Column(name = "usrupd")
    private String usrUpd;

    @Column(name = "dtmupd")
    private String dtmUpd;
}   
